#!/usr/bin/env bash
plutil -convert xml1 Payload/sosokan.app/Ringcaptcha.bundle/Info.plist
sed 's/iPhoneSimulator/iPhoneOS/' Payload/sosokan.app/Ringcaptcha.bundle/Info.plist
