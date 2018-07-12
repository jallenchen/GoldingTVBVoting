LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src) \
	src/com/golding/tvbvotingsystem/socket/ISocketService.aidl \
	src/aidl/IGoldingSysService.aidl

LOCAL_STATIC_JAVA_LIBRARIES :=protobuf eventbus supportV4 autobahn myglide

LOCAL_PROGUARD_ENABLED := custom
LOCAL_PROGUARD_FLAG_FILES := proguard-project.txt

LOCAL_PACKAGE_NAME := GoldingTVBVoting

LOCAL_CERTIFICATE := platform
include $(BUILD_PACKAGE)

##################################################
include $(CLEAR_VARS)

LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := protobuf:libs/protobuf_3.0.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += eventbus:libs/eventbus-3.0.0.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += supportV4:libs/android-support-v4.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += autobahn:libs/autobahn-0.5.0.jar
#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += myvolley:libs/myvolley.jar
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += myglide:libs/myglide-3.7.0.jar
#LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES += classes:libs/classes.jar

include $(BUILD_MULTI_PREBUILT)

include $(call all-makefiles-under,$(LOCAL_PATH))

