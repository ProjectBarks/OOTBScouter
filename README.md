# OOTB Scouter

This was a simple tool created for my robotics team Out Of The Box Robotics to enter data related to the [*FIRST* RES-Q](https://en.wikipedia.org/wiki/FIRST_RES-Q) challenge. All data would first be entered into the app, exported into a CSV, and finally imported FTC Scouter is a Material Design Android application focused in collecting match data and analyzing it. The data is serialized in JSON and then converted into a mySQL database where [Tableau](http://www.tableau.com/) visualizes the data.

### Dependencies

* [Material Dialogs](https://github.com/afollestad/material-dialogs) - Prompting for information
* [GSON](https://github.com/google/gson) - Efficent JSON De/Serialization
* [Swipe Layout](https://github.com/daimajia/AndroidSwipeLayout) - For Deletion of Entries
* [Ripple Effect](https://github.com/traex/RippleEffect) - Material Design Ripple Effect
* [MaterialEditText](https://github.com/rengwuxian/MaterialEditText) - Traditional Material Edit Style 
* [JExcelAPI](http://jexcelapi.sourceforge.net/) - Exporting Data As Excel Files

### Features

- [x] Data Entry 
- [x] Tablet Support
- [x] Previous Match Cache 
- [x] Data Export
- [x] Bluetooth Data Transfer
- [ ] Support For Future Competitions
- [ ] Live Data Sync

