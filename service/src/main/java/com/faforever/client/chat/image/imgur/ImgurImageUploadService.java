package com.faforever.client.chat.image.imgur;

import com.faforever.client.chat.image.ImageUploadService;
import com.faforever.client.task.TaskService;
import javafx.scene.image.Image;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;


@Lazy
@Service
public class ImgurImageUploadService implements ImageUploadService {

  private final TaskService taskService;
  private final ApplicationContext applicationContext;


  public ImgurImageUploadService(TaskService taskService, ApplicationContext applicationContext) {
    this.taskService = taskService;
    this.applicationContext = applicationContext;
  }

  @Override
  public CompletableFuture<String> uploadImageInBackground(Image image) {
    ImgurUploadTask imgurUploadTask = applicationContext.getBean(ImgurUploadTask.class);
    imgurUploadTask.setImage(image);
    return taskService.submitTask(imgurUploadTask).getFuture();
  }
}
