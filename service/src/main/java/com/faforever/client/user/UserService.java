package com.faforever.client.user;

import com.faforever.client.login.LoginFailedException;
import com.faforever.client.preferences.PreferencesService;
import com.faforever.client.remote.ErrorServerMessage;
import com.faforever.client.remote.FafService;
import com.faforever.client.task.CompletableTask;
import com.faforever.client.task.TaskService;
import com.google.common.eventbus.EventBus;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.CompletableFuture;

@Lazy
@Service
public class UserService implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private final StringProperty displayName;

  private final FafService fafService;
  private final PreferencesService preferencesService;
  private final EventBus eventBus;
  private final ApplicationContext applicationContext;
  private final TaskService taskService;
  private final ApplicationEventPublisher eventPublisher;

  private String password;
  private Integer userId;
  private CompletableFuture<Void> loginFuture;


  public UserService(
    FafService fafService,
    PreferencesService preferencesService,
    EventBus eventBus,
    ApplicationContext applicationContext,
    TaskService taskService,
    ApplicationEventPublisher eventPublisher
  ) {
    this.eventPublisher = eventPublisher;
    this.fafService = fafService;
    this.preferencesService = preferencesService;
    this.eventBus = eventBus;
    this.applicationContext = applicationContext;
    this.taskService = taskService;

    displayName = new SimpleStringProperty();
  }


  public CompletableFuture<Void> login(String username, String password, boolean autoLogin) {
    this.password = password;

    preferencesService.getPreferences().getLogin()
        .setUsername(username)
        .setPassword(password)
        .setAutoLogin(autoLogin);
    preferencesService.storeInBackground();

    loginFuture = fafService.connectAndLogIn(username, password)
        .thenAccept(accountDetails -> {
          userId = accountDetails.getId();

          // Because of different case (upper/lower)
          String displayName = accountDetails.getDisplayName();
          UserService.this.displayName.set(displayName);

          preferencesService.getPreferences().getLogin().setUsername(displayName);
          preferencesService.storeInBackground();

          LoginSuccessEvent event = new LoginSuccessEvent(username, displayName, password, userId);
          eventBus.post(event);
          eventPublisher.publishEvent(event);
        })
        .whenComplete((aVoid, throwable) -> {
          if (throwable != null) {
            fafService.disconnect();
          }
          loginFuture = null;
        });
    return loginFuture;
  }


  public String getDisplayName() {
    return displayName.get();
  }


  public String getPassword() {
    return password;
  }

  public Integer getUserId() {
    return userId;
  }


  public void cancelLogin() {
    if (loginFuture != null) {
      loginFuture.toCompletableFuture().cancel(true);
      loginFuture = null;
      fafService.disconnect();
    }
  }

  @EventListener
  public void onLoginError(ErrorServerMessage message) {
    if (loginFuture != null) {
      loginFuture.toCompletableFuture().completeExceptionally(new LoginFailedException(message.getText()));
      loginFuture = null;
      fafService.disconnect();
    }
  }

  public void logOut() {
    logger.info("Logging out");
    fafService.disconnect();
    eventBus.post(new LoggedOutEvent());
    preferencesService.getPreferences().getLogin().setAutoLogin(false);
  }


  public CompletableTask<Void> changePassword(String currentPassword, String newPassword) {
    ChangePasswordTask changePasswordTask = applicationContext.getBean(ChangePasswordTask.class);
    changePasswordTask.setUsername(displayName.get());
    changePasswordTask.setCurrentPassword(currentPassword);
    changePasswordTask.setNewPassword(newPassword);

    return taskService.submitTask(changePasswordTask);
  }

  @EventListener
  public void onAccountDetails(AccountDetailsServerMessage message) {
    userId = message.getId();
  }

  @Override
  public void afterPropertiesSet() {
    eventBus.register(this);
  }

  @EventListener(classes =  LogOutRequestEvent.class)
  public void onLogoutRequestEvent() {
    logOut();
  }
}
