# FReplaceModule

一个 Xposed 模块，用于将所有 UI 文本替换为自定义文字（默认 FFF）。

## 功能特性

- 替换系统 UI 文本
- 可配置的替换文本
- 支持 Android 5.0+ (API 21+)

## 构建说明

### 前置条件

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34

### 构建步骤

1. 克隆项目：
   ```bash
   git clone <repository-url>
   cd FReplaceModule-gradle
   ```

2. 用 Android Studio 打开项目

3. 等待 Gradle 同步完成

4. 构建 APK：
   ```bash
   ./gradlew assembleDebug
   ```

5. 安装到设备：
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

## 项目结构

```
FReplaceModule-gradle/
├── app/                          # 应用模块
│   ├── build.gradle.kts         # 模块构建配置
│   ├── proguard-rules.pro       # ProGuard 规则
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/             # Java/Kotlin 源码
│           └── res/              # 资源文件
├── build.gradle.kts             # 根构建配置
├── settings.gradle.kts          # 项目设置
├── gradle.properties            # Gradle 属性
└── gradle/wrapper/              # Gradle wrapper
```

## 使用说明

1. 安装 Xposed Framework
2. 安装此模块
3. 在 Xposed Installer 中启用模块
4. 重启设备或软重启
5. 打开模块设置界面配置替换文本

## 依赖项

- Xposed API 82
- AndroidX Core KTX 1.12.0
- AndroidX AppCompat 1.6.1
- Material Design 1.10.0

## 许可证

MIT License
