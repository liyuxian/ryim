
# react-native-imdemo

## Getting started

`$ npm install react-native-imdemo --save`

### Mostly automatic installation

`$ react-native link react-native-imdemo`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-imdemo` and add `RNImdemo.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNImdemo.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.wr.imdemo.RNImdemoPackage;` to the imports at the top of the file
  - Add `new RNImdemoPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-imdemo'
  	project(':react-native-imdemo').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-imdemo/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-imdemo')
  	```


## Usage
```javascript
import RNImdemo from 'react-native-imdemo';

// TODO: What to do with the module?
RNImdemo;
```
  