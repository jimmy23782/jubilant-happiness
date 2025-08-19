# syntax=docker/dockerfile:1.7

########## BUILD ##########
# Build stage uses a full JDK (compiler, tools) to compile your app.
# Keeping build tools out of the final image reduces attack surface and size (see multi-stage below).
FROM eclipse-temurin:17-jdk-jammy AS build 
WORKDIR /workspace

# Copy wrapper + build scripts (Groovy DSL)
# Copy only the Gradle wrapper and build scripts first.
# This lets Docker cache dependency resolution layers, so when you change code in src/, you don’t re-download dependencies.
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Warm wrapper & dependency cache (no source yet)
# Big benefit: massively speeds up rebuilds because Gradle’s artifact cache persists across builds without bloating image layers.
RUN --mount=type=cache,target=/root/.gradle \
    chmod +x gradlew && ./gradlew --no-daemon --version && \
    ./gradlew --no-daemon dependencies || true

# Copy source and build bootable jar
# Build the fat/bootable JAR efficiently with Gradle cache mounted.
# -x test skips tests in container builds (speeds up; you can run tests in CI separately).
COPY src src
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew clean build -x test --no-daemon

########## RUNTIME ##########
# Runtime stage uses a JRE (no compiler) → much smaller than JDK.
# Clean separation: build tools stay in the build image, not shipped to production.
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

### Run as non-root
#Runs the app as a non-root user.
#Security win: if the app or a dependency is compromised, it can’t easily escalate privileges or write to protected paths. Many platforms (Kubernetes, OpenShift) require non-root containers for admission.
RUN useradd -u 10001 -r -s /sbin/nologin appuser
USER 10001

# Copy the boot jar produced by bootJar { archiveFileName = "app.jar" }
#Copies only the final artifact from the build stage to the runtime stage.
#This is the essence of multi-stage builds → tiny runtime image, no build clutter.
COPY --from=build /workspace/build/libs/app.jar /app/app.jar

# Container-friendly JVM defaults (optional)
#more predictable memory usage and smoother GC in containers
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC -Dfile.encoding=UTF-8"

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]