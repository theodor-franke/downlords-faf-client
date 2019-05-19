#!/usr/bin/env bash

# TODO re-enable TLSv1.3
# See https://github.com/kt3k/coveralls-gradle-plugin/issues/85 and https://bugs.openjdk.java.net/browse/JDK-8221253
./gradlew -Djdk.tls.client.protocols="TLSv1,TLSv1.1,TLSv1.2" jacocoTestReport coveralls
