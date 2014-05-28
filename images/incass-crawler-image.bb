SUMMARY = "Console image for the INCASS Magnetic Crawler Robot with some C/C++ dev tools extented with ROS"
HOMEPAGE = "http://www.dfki.com"
LICENSE = "MIT"
 
IMAGE_FEATURES += "package-management ssh-server-openssh x11-base"
IMAGE_LINGUAS = "en-us"
USERNAME = "incass"
IPK_HOST ?= "eich-laptop"
HOSTNAME ?= "incass1"

inherit core-image extrausers

#add user with password 'incass'

EXTRA_USERS_PARAMS = "\
    useradd -p 'OTE7n9xS2KsNw' ${USERNAME}; \
"

#add extra ros packages here
ROS_EXTRA =" \
    roslaunch \
"   

#add dfki ros packages here
ROS_DFKI = "\
"

CORE_OS = " \
    busybox-hwclock \
    task-core-ssh-openssh openssh-keygen openssh-sftp-server \
    term-prompt \
    tzdata \
 "

# Custom kernel modules built out of tree
KERNEL_MODULES_OOT = " \
    omap3-pwm \
    omap3-mux \
    omap3-irqlat \
    hrt-test \
    udelay-test \    
 "

KERNEL_EXTRA_INSTALL = " \
    kernel-modules \
    ${KERNEL_MODULES_OOT} \
 "

WIFI_SUPPORT = " \
    iw \
    linux-firmware-rtl8192ce \
    linux-firmware-rtl8192cu \
    linux-firmware-sd8686 \
    linux-firmware-wl12xx \
    wpa-supplicant \
    wireless-tools \
 "

DEV_SDK_INSTALL = " \
    binutils \
    binutils-symlinks \
    coreutils \
    cpp \
    cpp-symlinks \
    diffutils \
    file \
    gcc \
    gcc-symlinks \
    g++ \
    g++-symlinks \
    gettext \
    git \
    ldd \
    libstdc++ \
    libstdc++-dev \
    libtool \
    make \
    pkgconfig \    
    libv4l \
    libv4l-dev \
 "

DEV_EXTRAS = " \
    avahi-daemon \
    ntp \
    ntp-tickadj \
 "

EXTRA_TOOLS_INSTALL = " \
    bzip2 \
    devmem2 \
    ethtool \
    findutils \
    i2c-tools \
    iftop \
    iperf \
    htop \
    less \
    procps \
    sysfsutils \
    tcpdump \
    unzip \
    wget \
    zip \
    vim \
    media-ctl \
    gstreamer \
    gst-plugins-good \    
    gst-plugins-good-video4linux2 \
    gst-plugins-bad-autoconvert \
    gst-plugins-base-theora \
    gst-plugins-good-rtp \
    gst-plugins-good-udp \
    cpufrequtils \
"

IMAGE_INSTALL += " \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${CORE_OS} \
    ${DEV_SDK_INSTALL} \
    ${DEV_EXTRAS} \
    ${EXTRA_TOOLS_INSTALL} \
    ${KERNEL_EXTRA_INSTALL} \
    ${WIFI_SUPPORT} \
    ${ROS_EXTRA} \
    ${ROS_DFKI} \
"
# packagegroup-ros-comm 

update_bash(){
   echo 'export ROS_ROOT=/usr' >> ${IMAGE_ROOTFS}/home/${USERNAME}/.bashrc
   echo 'export ROS_MASTER_URI=http://localhost:11311' >> ${IMAGE_ROOTFS}/home/${USERNAME}/.bashrc
   echo 'export CMAKE_PREFIX_PATH=/usr' >> ${IMAGE_ROOTFS}/home/${USERNAME}/.bashrc
   touch  ${IMAGE_ROOTFS}/usr/.catkin
}

set_local_timezone() {
    ln -sf /usr/share/zoneinfo/Europe/Berlin ${IMAGE_ROOTFS}/etc/localtime
}

disable_bootlogd() {
    echo BOOTLOGD_ENABLE=no > ${IMAGE_ROOTFS}/etc/default/bootlogd
}

update_opkg(){ 
   echo 'src all http://${IPK_HOST}/ipk/all' >>  ${IMAGE_ROOTFS}/etc/opkg/opkg.conf
   echo 'src overo http://${IPK_HOST}/ipk/overo' >> ${IMAGE_ROOTFS}/etc/opkg/opkg.conf
   echo 'src armv7a-vfp-neon http://${IPK_HOST}/ipk/armv7a-vfp-neon' >> ${IMAGE_ROOTFS}/etc/opkg/opkg.conf
}

update_hosts(){ 
  echo '127.0.1.1 \t ${HOSTNAME}' >> ${IMAGE_ROOTFS}/hosts
}


ROOTFS_POSTPROCESS_COMMAND_append = " \
    set_local_timezone ; \
    disable_bootlogd ; \
    update_bash; \
    update_opkg; \
    update_hosts; \
"

export IMAGE_BASENAME = "incass-crawler-image"
