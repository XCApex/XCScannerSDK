**扫码SDK接入文档**
-------------------

# 修改记录


| **版本号**             | **日期**   | **内容**                                                     |
| ---------------------- | ---------- | ------------------------------------------------------------ |
| 1.0.0                  | 2023/02/03 | 实现基本的扫码结果回调以及参数设置                           |
| 1.0.3                  | 2023/02/12 | 增加更多的扫码控制及配置接口。                               |
| 1.0.4                  | 2023/02/27 | 增加扫码服务的暂停和继续                                     |
| 1.0.6                  | 2023/03/09 | 支持版本信息、连续扫码、多条码支持及精确扫码相关接口。       |
| 1.0.7                  | 2023/03/10 | 支持瞄准灯和补光灯控制接口。                                 |
| 1.0.8                  | 2023/03/13 | 修订文档中的sdk版本号.                                       |
| 1.0.9                  | 2023/03/14 | 增加支持授权激活及授权状态查询的API。                        |
| 1.1.0                  | 2023/03/15 | 增加接口用于查询扫码服务的挂起状态。                         |
| 1.1.2                  | 2023/04/03 | 增加接口用于获取最后一张解码图片。                           |
| 1.1.3                  | 2023/04/11 | 增加接口用于设置扫码输出的第二个前缀及后缀参数。             |
| 1.1.8                  | 2024/05/16 | 增加自定义广播、返回条码类型结果的回调、扫码失败提示音、闪光灯亮度、禁用/启用扫码快捷按键、导出/导入配置文件接口。 |
| 1.1.9                  | 2024/08/26 | 增加获取/配置EAN13、Matrix25、UPCA条码属性接口。             |
| 1.1.10                 | 2024/09/24 | 增加获取/配置扫码触发模式接口。                              |
| 1.1.11                 | 2024/10/10 | 增加获取/配置Code39、DATAMATRIX、EAN8条码属性接口。          |
| 1.1.12                 | 2024/10/17 | 增加获取/配置code11、coded49、code93、code128、codeabar条码属性接口。 |
| 1.1.13                 | 2024/10/18 | 增加获取/配置GS1-128、GS1-DATABAR、ITF25、MSI、QRCode、UPCE条码属性接口。 |
| 1.1.14                 | 2024/11/21 | 增加设置扫码提示音接口。                                     |
| 1.1.15                 | 2024/12/10 | 增加Datamatrix码制可以切换和单独控制。                       |
| 1.1.16                 | 2024/12/13 | 增加Datamatrix和QrCode码制最大长度功能来替换最大输出长度功能 |
| 1.1.17                 | 2025/01/23 | 增加获取/配置是否在条码中显示AimID的接口                     |
| 1.1.18                 | 2025/02/26 | 增加设置传输键值状态的接口                                   |
| 1.3.49.0.13 （1.1.19） | 2025/04/17 | 增加获取/设置扫码白名单状态的接口，增加获取和设置扫码白名单的接口 |
>>>>>>> 8210683 (SDK 1.1.17  Add API to Use AimID barcode type switch interface.)

# 功能使用

## SDK初始化

最简单的情况下SDK初始化后，就可以使用扫码服务。目前提供两种初始化方式：

```java
 XcBarcodeScanner.init(Context context, ScannerResult scannerResult)

 XcBarcodeScanner.init(Context context, ScannerSymResult scannerSymResult)
```

回调类：

```java
    public interface ScannerResult {
        void onResult(String result); // 仅返回条码结果
    }

    public interface ScannerSymResult {
        void onResult(String sym, String barCode); // 返回条码结果和条码类型
    }
```

扫码SDK初始化后，就和系统的扫码服务建立了连接，此时扫码结果会通过ScannerResult/ScannerSymResult回调通知回来。

示例代码：

```java
        XcBarcodeScanner.init(this, new ScannerResult() {
            @Override
            public void onResult(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allResult = allResult + "\n" + result;
                        mTextResult.setText(allResult);
                        scrollToBottom();
                    }
                });
            }
        });

        XcBarcodeScanner.init(this, new ScannerSymResult() {
            @Override
            public void onResult(String sym, String barCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allResult = allResult + "\n" + sym + ":" + barCode;
                        mTextResult.setText(allResult);
                        scrollToBottom();
                    }
                });
            }
        });
```

## SDK反初始化

在Activity消亡或切换到后台时，需要进行反初始化动作，避免再次初始化后回调异常。

示例代码：

```java
        @Override
        protected void onPause() {
            super.onPause();
  
            XcBarcodeScanner.deInit(this);
        }
```

## 查询授权状态

使用如下接口可以查询授权状态：

```java
    XcBarcodeScanner.getLicenseState();
```

所有的授权状态定义请参考类： *LicenseState*

```java
    public class LicenseState {
        public static final int INACTIVE = 0;   // License not actived.
        public static final int ACTIVATING = 1; // License is activating.
        public static final int ACTIVED = 2;    // License already actived.
        public static final int INVALID = 3;    // License invalid, contact vendor.
        public static final int NETWORK_ISSUE = 4; // Network issue, connet before active license.
        public static final int EXPIRED = 5;    // License expired, contact vendor.
    }
```

## 激活授权

使用如下接口可以按需要激活授权。

```java
    XcBarcodeScanner.activateLicense();
```

在调用激活授权后，需要约1~2分钟时间完成激活流程。
我们可以通过接口 *getLicenseState* 来查询授权的状态变化。

**Note:** 激活授权需要有良好的网络连接。



## 重新激活授权

使用如下接口可以清除缓存重新激活授权。

```java
	XcBarcodeScanner.forceNetWorkActivate();
```

在调用激活授权后，需要约1~2分钟时间完成激活流程。
我们可以通过接口 *getLicenseState* 来查询授权的状态变化。

**Note:** 激活授权需要有良好的网络连接。



## 开始/结束扫码动作

可以通过SDK提供的接口控制扫码服务触发扫码动作的开始和结束。

```java
    XcBarcodeScanner.startScan();
    XcBarcodeScanner.stopScan();
```

## 暂停/继续扫码服务

可以通过SDK提供的接口控制扫码服务的暂停和继续。

```java
    XcBarcodeScanner.suspendScanService();
    XcBarcodeScanner.resumeScanService();
```

## 获取扫码服务的挂起状态

可以使用如下接口获取当前扫码服务的挂起状态。

```java
    boolean isScanServiceSuspending();
```

## 获取扫码服务版本号

可以通过SDK提供的接口获取扫码服务版本信息。

```java
    String serviceVer = XcBarcodeScanner.getServiceVersion();
    Log.d(TAG, "Service ver: " + serviceVer);
```

## 获取SDK版本号

可以通过SDK提供的接口获取SDK自己的版本信息。

```java
    String sdkVer = XcBarcodeScanner.getSdkVersion();
    Log.d(TAG, "SDK ver: " + sdkVer);
```

# 进阶设置

## 开关条码类型支持

可以通过SDK提供的接口控制扫码服务开启或关闭制定的条码类型。

```java
    XcBarcodeScanner.enableBarcodeType(String barcodeType, boolean enabled);
```

所有扫码类型都定义在BarcodeType类中

```java
    public class BarcodeType {
        public static final String AZTEC = "Aztec";
        public static final String CODE11 = "Code11";
        public static final String CODE39 = "Code39";
        public static final String CODE93 = "Code93";
        public static final String CODE128 = "Code128";
        public static final String CODABAR = "Codabar";
        public static final String DOTCODE = "Dotcode";
        public static final String CODABLOCKF = "CODABLOCK F";
        public static final String DATAMATRIX = "DATAMATRIX";
        public static final String GS1_DATAMATRIX = "GS1DATAMATRIX";
        public static final String EAN8 = "EAN-8";
        public static final String EAN13 = "EAN-13";
        // GS1 DataBar（前身为 GS1 DataBar-14）
        public static final String GS1_DATABAR = "GS1_DATABAR";
        public static final String HANXIN = "HANXIN";
        public static final String HK25 = "HK25";
        public static final String ITF25 = "ITF25";
        public static final String MATRIX25 = "MATRIX25";
        public static final String MAXICODE = "MAXICODE";
        public static final String MSI = "MSI";
        public static final String MICROPDF = "MICROPDF";
        public static final String PDF417 = "PDF417";
        public static final String USPS4ST = "USPS4ST";
        public static final String QRCODE = "QRCODE";
        public static final String INDUSTRIAL25 = "INDUSTRIAL25";
        public static final String TELEPEN = "TELEPEN";
        public static final String UPCA = "UPC-A";
        public static final String UPCE = "UPC-E";
        public static final String GS1_128 = "GS1-128";
        public static final String GS1_DATABAR_LIMITED = "GS1_DataBarLimited";
        public static final String GS1_DATABAR_EXPANDED = "GS1_DataBarExpanded";
        public static final String IATA25 = "IATA25";
        public static final String GRIDMATRIX = "Grid_Matrix";
    }
```

示例代码：

```java
    XcBarcodeScanner.enableBarcodeType(BarcodeType.QRCODE, true); // 开启QRCode支持
    XcBarcodeScanner.enableBarcodeType(BarcodeType.QRCODE, false); // 关闭QRCode支持
```

## 查询指定条码类型当前是否开启

所有扫码类型都定义在BarcodeType类中。

```java
    boolean isBarcodeTypeEnabled(String type)
```

## 输出方式设置

可以通过SDK提供的接口控制扫码服务输出扫码结果的方式。

```java
    XcBarcodeScanner.setOutputMethod(String method);
```

所有输出方式都定义在OutputMethod类中

```java
    public class OutputMethod {
        public static final String NONE = "NONE"; // No putput, application can get barcode data via callback.
        public static final String BROADCAST = "BROADCAST_EVENT"; // Output barcode data via broadcast.
        public static final String KEYBOARD = "KEYBOARD_EVENT"; // Output barcode data via keyboard simulate.
        public static final String CLIPBOARD = "CLIPBOARD_EVENT"; // Output barcode data via clipboard simulate.
        public static final String BROADCAST_KEYBOARD = "BROADCAST_EVENT/KEYBOARD_EVENT"; // Output barcode data via broadcast and keyboard simulate.
        public static final String BROADCAST_CLIPBOARD = "BROADCAST_EVENT/CLIPBOARD_EVENT"; // Output barcode data via broadcast and clipboard simulate.
    }
```

示例代码：

```java
    XcBarcodeScanner.setOutputMethod(OutputMethod.CLIPBOARD);
```

## 扫码提示设置

可以通过SDK提供的接口设置扫码成功提示。

```java
    XcBarcodeScanner.setSuccessNotification(String notification);
```

所有提示类型都定义在NotificationType类中

```java
    public class NotificationType {
        public static final String MUTE = "Mute";
        public static final String SOUND = "Sound";
        public static final String VIBRATOR = "Vib";
        public static final String SOUND_VIBRATOR = "Sound/Vib";
    }
```

示例代码：

```java
    XcBarcodeScanner.setSuccessNotification(NotificationType.SOUND);
```

## 扫码指示灯设置

可以通过SDK提供的接口设置扫码成功指示灯功能开关。

```java
    XcBarcodeScanner.enableSuccessIndicator(boolean enabled);
```

示例代码：

```java
    XcBarcodeScanner.enableSuccessIndicator(true); // 开启扫码指示灯功能
    XcBarcodeScanner.enableSuccessIndicator(false); // 关闭扫码指示灯功能
```

## 扫码超时设置

可以通过SDK提供的接口设置扫码超时时间。

```java
    XcBarcodeScanner.setTimeout(int seconds);
```

超时时间范围为1~9秒。

示例代码：

```java
    XcBarcodeScanner.setTimeout(5); // 设置超时时间为5秒
```

## 瞄准灯工作模式设置

可以通过SDK提供的接口设置瞄准灯的工作模式。

```java
    void setAimerLightsMode(int aimMode);
```

瞄准灯支持的工作模式在AimerMode类中定义：

```java
public class AimerMode {
    public static int ALWAYS_OFF; // Always Off.
    public static int TRIGGER_ON; // TurnOn while scanning.
    public static int ALWAYS_ON; // Always On.
}
```

示例代码：

```java
    XcBarcodeScanner.setAimerLightsMode(AimerMode.TRIGGER_ON); // 设置瞄准灯工作模式为：扫码时亮起
    XcBarcodeScanner.setAimerLightsMode(AimerMode.ALWAYS_OFF); // 设置瞄准灯工作模式为：一直关闭
```

**注意：** 如果激光瞄准灯，请勿使用常亮（ALWAYS_ON）工作模式。

## 补光灯工作模式设置

可以通过SDK提供的接口设置补光灯的工作模式。

```java
    void setFlashLightsMode(int flashMode);
```

补光灯支持的工作模式在FlashMode类中定义：

```java
public class FlashMode {
    public static int OFF;           // Always off.
    public static int ILLUME_ONLY;   // Only illume.
    public static int ILLUME_STROBE; // Illume and Strobe.
}
```

示例代码：

```java
    XcBarcodeScanner.setFlashLightsMode(FlashMode.ILLUME_STROBE); // 设置补光灯工作模式为：补光+闪光
    XcBarcodeScanner.setFlashLightsMode(FlashMode.ILLUME_ONLY);   // 设置补光灯工作模式为：仅补光
```

**注意：** 补光灯工作时，仅会在扫码时点亮，闪光（STROBE）模式会在图像传感器曝光的瞬间输出高亮光。


## 扫码结果大小写设置

可以通过SDK提供的接口设置扫码结果的大小写转换选项。

```java
    XcBarcodeScanner.setTextCase(String textCase);
```

所有转换选项都定义在TextCaseType类中

```java
    public class TextCaseType {
        public static final String NONE = "NONE_CASE";
        public static final String UPPER = "UPPER_CASE";
        public static final String LOWER = "LOWER_CASE";
    }
```

示例代码：

```java
    XcBarcodeScanner.setTextCase(TextCaseType.NONE);  // 设置扫码结果大小写不转换
    XcBarcodeScanner.setTextCase(TextCaseType.UPPER); // 设置扫码结果转换为大写
    XcBarcodeScanner.setTextCase(TextCaseType.LOWER); // 设置扫码结果转换为小写
```

## 扫码结果前缀设置

可以通过SDK提供的接口为扫码结果添加指定的前缀。

```java
    XcBarcodeScanner.setTextPrefix(String prefix);
    XcBarcodeScanner.setTextPrefix2(String prefix2);
```

示例代码：

```java
    XcBarcodeScanner.setTextPrefix("<");  // 设置扫码结果前缀为“<”
    XcBarcodeScanner.setTextPrefix2(":");  // 设置扫码结果前缀2为“:”
    XcBarcodeScanner.setTextPrefix("Empty"); // 设置扫码结果前缀为空
```

## 扫码结果后缀设置

可以通过SDK提供的接口为扫码结果添加指定的后缀。

```java
    XcBarcodeScanner.setTextSuffix(String prefix);
    XcBarcodeScanner.setTextSuffix1(String prefix2);
```

示例代码：

```java
    XcBarcodeScanner.setTextSuffix(">");  // 设置扫码结果后缀为“>”
    XcBarcodeScanner.setTextSuffix1(":");  // 设置扫码结果后缀2为“:”
    XcBarcodeScanner.setTextSuffix("Empty"); // 设置扫码结果后缀为空
```

## 设置连续扫码间隔时间

可以通过SDK提供的接口设置连续扫码的间隔时间。

```java
    setLoopScanInterval(int ms);
```

示例代码：

```java
    XcBarcodeScanner.setLoopScanInterval(100); // 设置连续扫码间隔时间为100毫秒
```

## 获取连续扫码状态

可以通过SDK提供的接口获取当前是否正在进行连续扫码。

```java
    boolean isLoopScanRunning();
```

## 开始/停止连续扫码

可以通过SDK提供的接口触发连续扫码。

```java
    startLoopScan();
    stopLoopScan();
```

示例代码：

```java
    // 开始连续扫码
    XcBarcodeScanner.setLoopScanInterval(100); // 设置连续扫码间隔时间为100毫秒
    XcBarcodeScanner.startLoopScan(); // 开始连续扫码
  
    // 结束连续扫码
    if (XcBarcodeScanner.isLoopScanRunning()) { // 检查当前是否正在连续扫码
      XcBarcodeScanner.stopLoopScan(); // 停止连续扫码
    }
```

## 设置多条码参数

可以通过SDK提供的接口，设置多条码扫描参数。

```java
    void setMultiBarcodes(int numberOfBarcodes, boolean fixedNumber);
```

参数说明：

```text
    numberOfBarcodes - 最大单次扫描的条码数量，范围：1~20
    fixedNumber - 是否固定条码数量，true表示固定条码数量，false表示非固定条码数量。
```

注：固定条码数量是指必须达到指定数量的条码才能解码成功；非固定条码数量则是解出了几个条码就输出几个条码。

示例代码：

```java
    // 设置多条码数量为3，且固定条码数量
    XcBarcodeScanner.setMultiBarcodes(3, true);
  
    // 设置多条码数量为1（每次只尝试解出1个条码）
    XcBarcodeScanner.setMultiBarcodes(1, false);
```

注：当多条码数量设置为1时，就是默认的单条码模式，此时固定条码数量的设置值无实际意义。

## 设置条码识别范围

可以通过SDK提供的接口，设置条码识别的范围，典型应用场景为一维码精准扫描。

```java
    void setScanRegionSize(int regionSize);
```

支持的regionSize定义在RegionSizeType类中：

```java
    public class RegionSizeType {
        public static final int VIEWSIZE_100 = 0; // 100% 画幅
        public static final int VIEWSIZE_75 = 1;  // 75% 画幅
        public static final int VIEWSIZE_50 = 2;  // 50% 画幅
        public static final int VIEWSIZE_25 = 3;  // 25% 画幅
        public static final int VIEWSIZE_12 = 4;  // 12% 画幅
        public static final int VIEWSIZE_6 = 5;  // 6% 画幅
        public static final int VIEWSIZE_3 = 6;  // 3% 画幅
        public static final int VIEWSIZE_1 = 7;  // 1% 画幅
        public static final int VIEWSIZE_1D = 8;  // 一维码专用画幅（图像的中间几行）
    }
```

注：固定条码数量是指必须达到指定数量的条码才能解码成功；非固定条码数量则是解出了几个条码就输出几个条码。

示例代码：

```java
    // 设置条码识别精度为一维码精准识别
    XcBarcodeScanner.setScanRegionSize(RegionSizeType.VIEWSIZE_1D);
  
    // 设置条码识别精度为最低（整个100%画面范围内的条码都识别）
    XcBarcodeScanner.setScanRegionSize(RegionSizeType.VIEWSIZE_100);
```

注：如果开启了多条码识别，推荐使用100%画幅的识别范围。

## 获取最后一张解码图片

可以通过以下接口获取最后一张送去解码的图片。

```java
    XCImage getLastDecodeImage();
```

***XCImage*** 是返回的图片对象。

示例代码：

```java
import com.tools.XCImage;

public void getLastImage() {
    XCImage lastImg=XcBarcodeScanner.getLastDecodeImage();
    
    if(lastImg!=null){
        String infoStr="Witdh: "+lastImg.getWidth()+", Height: "+lastImg.getHeight()+", Stride: "+lastImg.getStride()+", size: "+lastImg.getData().length+" Bytes";
        showAlertDialog("Image Info:",infoStr,false,"OK",null);
    }else{
        showAlertDialog("Image Info:","No image!",false,"OK",null);
    }
}
```

## 设置自定义广播

将自定义广播的Action和接受扫码结果的Key通过该接口配置好后，就可以通过广播接收到扫码结果了。

```java
void setScanResultBroadcast(String action, String resultKey);
```

使用示例：

```java
XcBarcodeScanner.setScanResultBroadcast("xxx.Action", "scanResultKey");
```

## 禁用/启用扫码快捷按键

根据设备设计不同，目前所支持的快捷键有：左侧边扫码键/右侧边扫码键/正面扫码键/手持柄扫码键。针对不同的快捷键，提供了对应的禁用/启用扫码功能接口。禁用后，按下扫码按键将无法扫码；启用后，按键恢复扫码功能。

```java
void setFrontScanKeyEnable(boolean isEnable); //正面扫码按键

void setLeftScanKeyEnable(boolean isEnable); //左侧边扫码键

void setRightScanKeyEnable(boolean isEnable); //右侧边扫码键

void setPoGoScanKeyEnable(boolean isEnable); //手持柄扫码键
```

使用示例：

```java
XcBarcodeScanner.setFrontScanKeyEnable(false); //禁用正面扫码按键扫码功能

XcBarcodeScanner.setFrontScanKeyEnable(true); //启用正面扫码按键扫码功能
```

## 导出配置文件

导出当前正在使用的配置文件到指定目录。

```java
XcBarcodeScanner.exportSettings(String exportPath);
```

导出文件的格式必须是xml类型，文件名只能包含字母和数字。示例代码：

```java
String exportPath = Environment.getExternalStorageDirectory().getPath() + "/Scanner.xml";
XcBarcodeScanner.exportSettings(exportPath);  //将配置文件导出到sdcard下，并重命名为Scanner.xml
```

## 导入配置文件

可以通过SDK提供的接口，使用指定目录下的配置文件。

```java
XcBarcodeScanner.importSettingsByProfileName(String profileName, String importPath);
```

**注意：**配置文件名只能包含字母和数字。且<u>**配置文件必须是通过 “XcBarcodeScanner.exportSettings” 接口导出的文件**</u>。

示例代码：

```java
String fileName = "Scanner";   //文件名不能包含类型后缀
String importPath = Environment.getExternalStorageDirectory().getPath() + "/Scanner.xml";
XcBarcodeScanner.importSettingsByProfileName(fileName, importPath);
```

## 配置管理

导入、导出或者重置扫码配置

```java
    void loadSettings();//导入设置
    void saveSettings();//导出设置
    void resetSettings();//重置设置
```

## DataMatrix条码配置

可以通过SDK提供的接口对DataMatrix进行设置。

```java
   void setDataMatrixWithSeparators(boolean withSeparators);//设置显示分隔符，true显示 false 不显示
   void setDataMatrixMaxOutputLength(int maxLength);//设置最大输出长度（0:不限制）
```

示例代码：

```java
    
    XcBarcodeScanner.setDataMatrixWithSeparators(true); //开启DataMatrix的分隔符显示
    
    XcBarcodeScanner.setDataMatrixMaxOutputLength(2); // 设置DataMatrix解码的最大输出长度为2
   
```



## 扫码失败提示音

可以通过SDK提供的接口设置扫码失败提示音。

```java
XcBarcodeScanner.setFailNotification(String notification);
```

所有提示类型都定义在NotificationType类中：

```java
public class NotificationType {
    public static final String MUTE = "Mute";
    public static final String SOUND = "Sound";
    public static final String VIBRATOR = "Vib";
    public static final String SOUND_VIBRATOR = "Sound/Vib";
}
```

示例代码：

```
XcBarcodeScanner.setFailNotification(NotificationType.MUTE);   //关闭扫码失败提示音
XcBarcodeScanner.setFailNotification(NotificationType.SOUND);  //开启扫码失败提示音
```

## 闪光灯亮度

可以通过SDK提供的接口设置闪光灯亮度。

```java
XcBarcodeScanner.setStrobeLightBrightness(int brightness);
```

闪光灯亮度支持在StrobeLightBrightness类中定义：

```java
public class StrobeLightBrightness {
    public static int FULL_BRIGHTNESS = 4;
    public static int MEDIUM_BRIGHTNESS = 7;
    public static int WEAK_BRIGHTNESS = 5;
    public static int WEAKEST_BRIGHTNESS = 6;
}
```

示例代码：

```
XcBarcodeScanner.setStrobeLightBrightness(StrobeLightBrightness.WEAK_BRIGHTNESS);  //设置闪光灯亮度为弱亮度
```

## 获取条码属性

可以通过该接口获取指定类型码制的属性支持情况。

```java
int getDecoderTagValue(int tag);
```

支持查询的属性定义在XCBarcodeTag类中：

```
public class XCBarcodeTag {
    // Code11
    // 最小长度（6-127）。取值范围为6-127的整数。
    public static final int TAG_CODE11_MIN_LENGTH            = 0x1A01E002;
    // 最大长度（6-127）。取值范围为6-127的整数。
    public static final int TAG_CODE11_MAX_LENGTH            = 0x1A01E003;
    // 校验码选项
    // 0：校验两位并输出校验码；1：校验一位并输出校验码；2：校验两位不输出校验码；
    // 3：校验一位不输出校验码；4：不校验
    public static final int TAG_CODE11_CHECK_DIGIT_MODE      = 0x1A01E004;

    // Code39
    // 校验码选项。0：关闭校验；1：开启校验不输出；2：开启校验并输出。
    public static final int TAG_CODE39_CHECK_DIGIT_MODE      = 0x1A016004;
    // 输出起始和结束字符。1：开启；0：关闭
    public static final int TAG_CODE39_START_STOP_TRANSMIT   = 0x1A016007;
    // Code 39 Full ASCII。1：开启；0：关闭
    public static final int TAG_CODE39_FULL_ASCII_ENABLED    = 0x1A016006;
    // 支持Code32解码。1：开启；0：关闭
    public static final int TAG_CODE39_BASE32_ENABLED        = 0x1A016008;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE39_MAX_LENGTH            = 0x1A016003;
    // 最小长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE39_MIN_LENGTH            = 0x1A016002;
    
    // Code49
    // 最小长度（1-127）。取值范围为1-127的整数。 
    public static final int TAG_CODE49_MIN_LENGTH            = 0x0C035002;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE49_MAX_LENGTH            = 0x0C035003;
    
    // Code93
    // 最大长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_CODE93_MAX_LENGTH            = 0x1A01D003;
    // 最小长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_CODE93_MIN_LENGTH            = 0x1A01D002;
    
    // code128
    // 显示分隔符。1：显示；0：不显示。
    public static final int TAG_C128_SEPARATOR_ENABLED       = 0x1A014006;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE128_MAX_LENGTH           = 0x1A014003;
    // 最小长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE128_MIN_LENGTH           = 0x1A014002;
    
    // Codabar
    // 校验码选项。0：关闭校验；1：开启校验不输出；2：开启校验并输出。
    public static final int TAG_CODABAR_CHECK_DIGIT_MODE     = 0x1A01F005;
    // 输出起始和结束字符。1：开启；0：关闭。
    public static final int TAG_CODABAR_START_STOP_TRANSMIT  = 0x1A01F004;
    // 最小长度（4-127）。取值范围为4-127的整数。
    public static final int TAG_CODABAR_MIN_LENGTH           = 0x1A01F002;
    
    // DataMatrix
    // 显示分隔符。1：显示；0：隐藏
    public static final int TAG_DATAMATRIX_SEPARATOR_ENABLED = 0x1A029004;
    // 最大输出长度（0：不限制）。大于等于0的整数，0表示不限制。
    public static final int TAG_DATAMATRIX_OUTPUT_MAX_LENGTH = 0x1A029005;
    // 最大长度（1-3116）。取值范围为1-3116的整数。
    public static final int TAG_DATAMATRIX_MAX_LENGTH        = 0x1A029003;

    // EAN-8
    // 输出校验码。1：开启；0：关闭
    public static final int TAG_EAN8_CHECK_DIGIT_TRANSMIT    = 0x1A012002;
    // 支持两位附加码。1：开启；0：关闭
    public static final int TAG_EAN8_2CHAR_ADDENDA_ENABLED   = 0x1A012003;
    // 支持五位附加码。1：开启；0：关闭
    public static final int TAG_EAN8_5CHAR_ADDENDA_ENABLED   = 0x1A012004;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_EAN8_ADDENDA_REQUIRED        = 0x1A012005;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_EAN8_ADDENDA_SEPARATOR       = 0x1A012006;
    
    // EAN-13
    // 输出校验码。1：开启；0：关闭
    public static final int TAG_EAN13_CHECK_DIGIT_TRANSMIT   = 0x1A013002;
    // 支持2位附加码。1：开启；0：关闭
    public static final int TAG_EAN13_2CHAR_ADDENDA_ENABLED  = 0x1A013003;
    // 支持5位附加码。1：开启；0：关闭
    public static final int TAG_EAN13_5CHAR_ADDENDA_ENABLED  = 0x1A013004;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_EAN13_ADDENDA_REQUIRED       = 0x1A013005;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_EAN13_ADDENDA_SEPARATOR      = 0x1A013006;

    // GS1 128
    // 显示分隔符。1：显示；0：隐藏
    public static final int TAG_GS1_128_SEPARATOR_ENABLED    = 0x1A015004;
    
    // GS1 DATABAR（GS1 DataBar-14）
    // GS1 DataBar Limited。1：开启；0：关闭
    public static final int TAG_RSS_LIMITED_ENABLED          = 0x1A022002;
    // GS1 DataBar Expanded。1：开启；0：关闭
    public static final int TAG_RSS_EXPANDED_ENABLED         = 0x1A022003;
    
    // ITF25
    // 校验码选项。0：不校验；1：校验但不输出校验码；2：校验并输出校验码。
    public static final int TAG_I25_CHECK_DIGIT_MODE         = 0x1A019004;
    // 最大长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_I25_MAX_LENGTH               = 0x1A019003;
    // 最小长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_I25_MIN_LENGTH               = 0x1A019002;
    
    // Matrix 2 of 5
    // 校验码选项。0：关闭校验；1：开启校验并输出；2：开启校验不输出。
    public static final int TAG_M25_CHECK_DIGIT_MODE         = 0x1A01C004;
    
    // MSI
    // 校验码选项。0：关闭；1：模式10并输出；2：模式10但不输出；
    // 3：模式10/10并输出；4：模式10/10但不输出；5：模式11/10并输出；6：模式11/10但不输出。
    public static final int TAG_MSI_CHECK_DIGIT_MODE         = 0x1A021004;
    // 最小长度（0-55）。取值范围为0-55的整数。
    public static final int TAG_MSI_MIN_LENGTH               = 0x1A021002;
    
    // QRCode
    // MicroQR支持。1：开启；0：关闭。
    public static final int TAG_QR_ENABLED                   = 0x1A02A001;
    // 最大输出长度（0：不限制）。
    public static final int TAG_QR_MAX_OUTPUT_LENGTH         = 0x1A02A004;
    // 最大长度（0-7089）。取值范围为0-7089的整数。
    public static final int TAG_QR_MAX_LENGTH                = 0x1A02A003;

    // UPC-A
    // 输出校验码。1：开启；0：关闭 
    public static final int TAG_UPCA_CHECK_DIGIT_TRANSMIT    = 0x1A010002;
    // 输出数制码。1：开启；0：关闭 
    public static final int TAG_UPCA_NUMBER_SYSTEM_TRANSMIT  = 0x1A010003;
    // 支持2位附加码。1：开启；0：关闭
    public static final int TAG_UPCA_2CHAR_ADDENDA_ENABLED   = 0x1A010004;
    // 支持5位附加码。1：开启；0：关闭
    public static final int TAG_UPCA_5CHAR_ADDENDA_ENABLED   = 0x1A010005;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_UPCA_ADDENDA_REQUIRED        = 0x1A010006;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_UPCA_ADDENDA_SEPARATOR       = 0x1A010007;
    // 转换为EAN13。1：开启；0：关闭
    public static final int TAG_UPCA_ADD_COUNTRY_CODE        = 0x1A010008;
    
    // UPC-E
    // UPCE扩充。1：开启；0：关闭。
    public static final int TAG_UPCE_EXPAND                  = 0x1A011003;
    // 输出校验码。1：开启；0：关闭。
    public static final int TAG_UPCE_CHECK_DIGIT_TRANSMIT    = 0x1A011004;
    // 输出数制码。1：开启；0：关闭。
    public static final int TAG_UPCE_NUMBER_SYSTEM_TRANSMIT  = 0x1A011005;
    // 支持2位附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_2CHAR_ADDENDA_ENABLED   = 0x1A011006;
    // 支持5位附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_5CHAR_ADDENDA_ENABLED   = 0x1A011007;
    // 强制要求附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_ADDENDA_REQUIRED        = 0x1A011008;
    // 附加码前加分隔符。1：开启；0：关闭。
    public static final int TAG_UPCE_ADDENDA_SEPARATOR       = 0x1A011009;
}
```

示例代码：

```
// 获取EAN13“输出校验码”功能是否开启。返回值：1，开启；0，关闭。
int checkSumDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_EAN13_CHECK_DIGIT_TRANSMIT);

// 获取当前Matrix25校验码支持情况。返回值：0，关闭校验；1，开启校验并输出；2，开启校验不输出。
int checkDigitDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_M25_CHECK_DIGIT_MODE);

// 获取UPC-A“支持2位附加码”功能是否开启。返回值：1，开启；0，关闭。
int twoAddonDef = XcBarcodeScanner.getDecoderTagValue(XCBarcodeTag.TAG_UPCA_2CHAR_ADDENDA_ENABLED);
```

## 配置条码属性

可以通过该接口配置指定类型码制的属性。

```java
void setDecoderTag(int tag, int value);
```

支持配置的属性定义在XCBarcodeTag类中：

```
public class XCBarcodeTag {
    // Code11
    // 最小长度（6-127）。取值范围为6-127的整数。
    public static final int TAG_CODE11_MIN_LENGTH            = 0x1A01E002;
    // 最大长度（6-127）。取值范围为6-127的整数。
    public static final int TAG_CODE11_MAX_LENGTH            = 0x1A01E003;
    // 校验码选项
    // 0：校验两位并输出校验码；1：校验一位并输出校验码；2：校验两位不输出校验码；
    // 3：校验一位不输出校验码；4：不校验
    public static final int TAG_CODE11_CHECK_DIGIT_MODE      = 0x1A01E004;

    // Code39
    // 校验码选项。0：关闭校验；1：开启校验不输出；2：开启校验并输出。
    public static final int TAG_CODE39_CHECK_DIGIT_MODE      = 0x1A016004;
    // 输出起始和结束字符。1：开启；0：关闭
    public static final int TAG_CODE39_START_STOP_TRANSMIT   = 0x1A016007;
    // Code 39 Full ASCII。1：开启；0：关闭
    public static final int TAG_CODE39_FULL_ASCII_ENABLED    = 0x1A016006;
    // 支持Code32解码。1：开启；0：关闭
    public static final int TAG_CODE39_BASE32_ENABLED        = 0x1A016008;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE39_MAX_LENGTH            = 0x1A016003;
    // 最小长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE39_MIN_LENGTH            = 0x1A016002;
    
    // Code49
    // 最小长度（1-127）。取值范围为1-127的整数。 
    public static final int TAG_CODE49_MIN_LENGTH            = 0x0C035002;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE49_MAX_LENGTH            = 0x0C035003;
    
    // Code93
    // 最大长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_CODE93_MAX_LENGTH            = 0x1A01D003;
    // 最小长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_CODE93_MIN_LENGTH            = 0x1A01D002;
    
    // code128
    // 显示分隔符。1：显示；0：不显示。
    public static final int TAG_C128_SEPARATOR_ENABLED       = 0x1A014006;
    // 最大长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE128_MAX_LENGTH           = 0x1A014003;
    // 最小长度（1-127）。取值范围为1-127的整数。
    public static final int TAG_CODE128_MIN_LENGTH           = 0x1A014002;
    
    // Codabar
    // 校验码选项。0：关闭校验；1：开启校验不输出；2：开启校验并输出。
    public static final int TAG_CODABAR_CHECK_DIGIT_MODE     = 0x1A01F005;
    // 输出起始和结束字符。1：开启；0：关闭。
    public static final int TAG_CODABAR_START_STOP_TRANSMIT  = 0x1A01F004;
    // 最小长度（4-127）。取值范围为4-127的整数。
    public static final int TAG_CODABAR_MIN_LENGTH           = 0x1A01F002;
    
    // DataMatrix
    // 显示分隔符。1：显示；0：隐藏
    public static final int TAG_DATAMATRIX_SEPARATOR_ENABLED = 0x1A029004;
    // 最大输出长度（0：不限制）。大于等于0的整数，0表示不限制。
    public static final int TAG_DATAMATRIX_OUTPUT_MAX_LENGTH = 0x1A029005;
    // 最大长度（1-3116）。取值范围为1-3116的整数。
    public static final int TAG_DATAMATRIX_MAX_LENGTH        = 0x1A029003;

    // EAN-8
    // 输出校验码。1：开启；0：关闭
    public static final int TAG_EAN8_CHECK_DIGIT_TRANSMIT    = 0x1A012002;
    // 支持两位附加码。1：开启；0：关闭
    public static final int TAG_EAN8_2CHAR_ADDENDA_ENABLED   = 0x1A012003;
    // 支持五位附加码。1：开启；0：关闭
    public static final int TAG_EAN8_5CHAR_ADDENDA_ENABLED   = 0x1A012004;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_EAN8_ADDENDA_REQUIRED        = 0x1A012005;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_EAN8_ADDENDA_SEPARATOR       = 0x1A012006;
    
    // EAN-13
    // 输出校验码。1：开启；0：关闭
    public static final int TAG_EAN13_CHECK_DIGIT_TRANSMIT   = 0x1A013002;
    // 支持2位附加码。1：开启；0：关闭
    public static final int TAG_EAN13_2CHAR_ADDENDA_ENABLED  = 0x1A013003;
    // 支持5位附加码。1：开启；0：关闭
    public static final int TAG_EAN13_5CHAR_ADDENDA_ENABLED  = 0x1A013004;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_EAN13_ADDENDA_REQUIRED       = 0x1A013005;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_EAN13_ADDENDA_SEPARATOR      = 0x1A013006;

    // GS1 128
    // 显示分隔符。1：显示；0：隐藏
    public static final int TAG_GS1_128_SEPARATOR_ENABLED    = 0x1A015004;
    
    // GS1 DATABAR（GS1 DataBar-14）
    // GS1 DataBar Limited。1：开启；0：关闭
    public static final int TAG_RSS_LIMITED_ENABLED          = 0x1A022002;
    // GS1 DataBar Expanded。1：开启；0：关闭
    public static final int TAG_RSS_EXPANDED_ENABLED         = 0x1A022003;
    
    // ITF25
    // 校验码选项。0：不校验；1：校验但不输出校验码；2：校验并输出校验码。
    public static final int TAG_I25_CHECK_DIGIT_MODE         = 0x1A019004;
    // 最大长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_I25_MAX_LENGTH               = 0x1A019003;
    // 最小长度（2-127）。取值范围为2-127的整数。
    public static final int TAG_I25_MIN_LENGTH               = 0x1A019002;
    
    // Matrix 2 of 5
    // 校验码选项。0：关闭校验；1：开启校验并输出；2：开启校验不输出。
    public static final int TAG_M25_CHECK_DIGIT_MODE         = 0x1A01C004;
    
    // MSI
    // 校验码选项。0：关闭；1：模式10并输出；2：模式10但不输出；
    // 3：模式10/10并输出；4：模式10/10但不输出；5：模式11/10并输出；6：模式11/10但不输出。
    public static final int TAG_MSI_CHECK_DIGIT_MODE         = 0x1A021004;
    // 最小长度（0-55）。取值范围为0-55的整数。
    public static final int TAG_MSI_MIN_LENGTH               = 0x1A021002;
    
    // QRCode
    // MicroQR支持。1：开启；0：关闭。
    public static final int TAG_QR_ENABLED                   = 0x1A02A001;
    // 最大输出长度（0：不限制）。
    public static final int TAG_QR_MAX_OUTPUT_LENGTH         = 0x1A02A004;
    // 最大长度（0-7089）。取值范围为0-7089的整数。
    public static final int TAG_QR_MAX_LENGTH                = 0x1A02A003;

    // UPC-A
    // 输出校验码。1：开启；0：关闭 
    public static final int TAG_UPCA_CHECK_DIGIT_TRANSMIT    = 0x1A010002;
    // 输出数制码。1：开启；0：关闭 
    public static final int TAG_UPCA_NUMBER_SYSTEM_TRANSMIT  = 0x1A010003;
    // 支持2位附加码。1：开启；0：关闭
    public static final int TAG_UPCA_2CHAR_ADDENDA_ENABLED   = 0x1A010004;
    // 支持5位附加码。1：开启；0：关闭
    public static final int TAG_UPCA_5CHAR_ADDENDA_ENABLED   = 0x1A010005;
    // 强制要求附加码。1：开启；0：关闭
    public static final int TAG_UPCA_ADDENDA_REQUIRED        = 0x1A010006;
    // 附加码前加分隔符。1：开启；0：关闭
    public static final int TAG_UPCA_ADDENDA_SEPARATOR       = 0x1A010007;
    // 转换为EAN13。1：开启；0：关闭
    public static final int TAG_UPCA_ADD_COUNTRY_CODE        = 0x1A010008;
    
    // UPC-E
    // UPCE扩充。1：开启；0：关闭。
    public static final int TAG_UPCE_EXPAND                  = 0x1A011003;
    // 输出校验码。1：开启；0：关闭。
    public static final int TAG_UPCE_CHECK_DIGIT_TRANSMIT    = 0x1A011004;
    // 输出数制码。1：开启；0：关闭。
    public static final int TAG_UPCE_NUMBER_SYSTEM_TRANSMIT  = 0x1A011005;
    // 支持2位附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_2CHAR_ADDENDA_ENABLED   = 0x1A011006;
    // 支持5位附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_5CHAR_ADDENDA_ENABLED   = 0x1A011007;
    // 强制要求附加码。1：开启；0：关闭。
    public static final int TAG_UPCE_ADDENDA_REQUIRED        = 0x1A011008;
    // 附加码前加分隔符。1：开启；0：关闭。
    public static final int TAG_UPCE_ADDENDA_SEPARATOR       = 0x1A011009;
}
```

示例代码：

```
// 开启EAN13的“输出校验码”功能。参数：1，开启；0，关闭。
XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_EAN13_CHECK_DIGIT_TRANSMIT, 1);

// 设置Matrix25校验码支持为“开启校验不输出”。参数：0，关闭校验；1，开启校验并输出；2，开启校验不输出。
XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_M25_CHECK_DIGIT_MODE, 2);

// 关闭UPC-A的“支持2位附加码”功能。参数：1，开启；0，关闭。
XcBarcodeScanner.setDecoderTag(XCBarcodeTag.TAG_UPCA_2CHAR_ADDENDA_ENABLED, 0);
```

## 获取扫码触发模式

可以通过该接口获取当前的扫码触发模式。

```java
String getScanTriggerMode();
```

支持配置的属性定义在ScanTriggerMode类中：

```
public class ScanTriggerMode {
    public static final String STOP_ON_RELEASE = "SYNC"; //按键抬起时停止
    public static final String STOP_ON_TIMEOUT = "TIMEOUT"; //超时后停止
}
```

示例代码：

```
String defMode = XcBarcodeScanner.getScanTriggerMode();
```

## 配置扫码触发模式

可以通过该接口配置扫码触发模式。

```java
void setScanTriggerMode(String val);
```

支持配置的属性定义在ScanTriggerMode类中：

```
public class ScanTriggerMode {
    public static final String STOP_ON_RELEASE = "SYNC"; //按键抬起时停止
    public static final String STOP_ON_TIMEOUT = "TIMEOUT"; //超时后停止
}
```

示例代码：

```
XcBarcodeScanner.setScanTriggerMode(ScanTriggerMode.STOP_ON_RELEASE); //将扫码触发模式配置成按键抬起时停止

XcBarcodeScanner.setScanTriggerMode(ScanTriggerMode.STOP_ON_TIMEOUT); //将扫码触发模式配置成超时后停止
```

## 设置扫码提示音量

可通过此接口设置扫码音量，参数：0.0 - 1.0

```java
void setScanVolume(float volume)
```



## 获取显示AimID的状态

可以通过该接口获取显示AimID的状态。

```java
boolean getUseAimidInResult();
```



## 设置显示AimID的状态

可以通过该接口设置显示AimID的状态。

```java
void setUseAimidInResult(boolean isEnable);
```

示例代码：

```
//不显示AimID的结果到扫码结果中
if(getUseAimidInResult()){
	XcBarcodeScanner.setUseAimidInResult(false);
}
```

## 设置传输键值的状态

可以通过该接口设置传输键值的状态。

```java
 void setTransmitKeyEvent(boolean isEnable);
```

示例代码:

```
//关闭传输键值功能
XcBarcodeScanner.setTransmitKeyEvent(false);

```

## 

## 获取扫码白名单的状态

可以通过该接口获取扫码白名单的状态。

```java
boolean isScanWhiteListEnable();
```



## 设置扫码白名单的状态

可以通过该接口设置扫码白名单的状态。

```java
void setScanWhiteListEnable(boolean isEnable);
```

示例代码：

```
//关闭扫码白名单
if(isScanWhiteListEnable()){
	XcBarcodeScanner.setScanWhiteListEnable(false);
}
```

## 获取扫码白名单

可以通过该接口获取扫码白名单。

```java
String getScanWhiteListPkgs();
```

## 设置扫码白名单

可以通过该接口设置扫码白名单。

```java
 void addScanWhiteListPkgs(String whiteList);//添加扫码白名单
 void delScanWhiteListPkgs(String whiteList);//删除扫码白名单
```

示例代码：

```
//添加 "com.android.launcher3"和"com.android.deskclock"到扫码白名单
XcBarcodeScanner.addScanWhiteListPkgs("com.android.launcher3,com.android.deskclock");
//从扫码白名单中删除 "com.android.launcher3" 
XcBarcodeScanner.delScanWhiteListPkgs("com.android.launcher3");

```

