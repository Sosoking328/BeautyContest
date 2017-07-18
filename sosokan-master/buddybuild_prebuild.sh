#!/usr/bin/env bash
#plutil -convert xml1 Pods/Ringcaptcha/Ringcaptcha.bundle/Info.plist
#sed -i -e 's/iPhoneSimulator/iPhoneOS/g' Pods/Ringcaptcha/Ringcaptcha.bundle/Info.plist
/usr/libexec/PlistBuddy -c "Delete CFBundleSupportedPlatforms" Pods/Ringcaptcha/Ringcaptcha.bundle/Info.plist
/usr/libexec/PlistBuddy -c "Delete CFBundleExecutable" Pods/Ringcaptcha/Ringcaptcha.bundle/Info.plist
# Adding a key (BUDDYBUILD_BUILD_NUMBER) to the Plist
#/usr/libexec/PlistBuddy -c "Add BUDDYBUILD_BUILD_NUMBER String $BUDDYBUILD_BUILD_NUMBER"