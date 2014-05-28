SUMMARY = "Package to bring up wireless after boot using systemd"
HOMEPAGE = "http://www.dfki.com"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"
 
inherit systemd

PROVIDES="network-wireless"

do_install_append() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/network-wireless@.service ${D}${systemd_unitdir}/system
}
