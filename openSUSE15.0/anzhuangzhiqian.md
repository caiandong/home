### bios+msdos
  * 直接分区即可
  * /boot也可以不用单独分区
### bios+gpt
  * 由于bios不识别gpt，所以要分一个小空间为启动分区，通常10m左右
  * /boot也可以不用单独分区
### uefi+gpt
  * 通常第一个分/boot/efi 大约500mb，格式vfat。好像说是为了兼容windows，不太清楚
