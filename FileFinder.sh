#!/bin/bash
#!/bin/bash
# ---------------------------------------------------------------------------
# ootb_data_export - Exports match data from an Android device.

# Copyright 2015,  <brandon@Brandons-Air.home>

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License at <http://www.gnu.org/licenses/> for
# more details.

# Usage: ootb_data_export [-h|--help] [-a |--adb PATH] [-e|--export PATH]

# Revision history:
# 2015-02-17 Created by new_script.sh ver. 3.3
# ---------------------------------------------------------------------------

PROGNAME=${0##*/}
VERSION="0.1"

clean_up() { # Perform pre-exit housekeeping
  return
}

error_exit() {
  echo -e "${PROGNAME}: ${1:-"Unknown Error"}" >&2
  clean_up
  exit 1
}

graceful_exit() {
  clean_up
  exit
}

signal_exit() { # Handle trapped signals
  case $1 in
    INT)
      error_exit "Program interrupted by user" ;;
    TERM)
      echo -e "\n$PROGNAME: Program terminated" >&2
      graceful_exit ;;
    *)
      error_exit "$PROGNAME: Terminating on unknown signal" ;;
  esac
}

usage() {
  echo -e "Usage: $PROGNAME [-h|--help] [-a |--adb PATH] [-e|--export PATH]"
}

-e help_message() {
-e   cat <<- _EOF_
-e   $PROGNAME ver. $VERSION
-e   Exports match data from an Android device.
-e
  $(usage)
-e
  Options:
-e   -h, --help  Display this help message and exit.
-e   -a , --adb PATH  The path to adb
-e     Where 'PATH' is the The path to adb.
-e   -e, --export PATH  > Set the location of the exported xls
-e     Where 'PATH' is the The path of for the xls file.
-e
_EOF_
-e   return
}

# Trap signals
trap "signal_exit TERM" TERM HUP
trap "signal_exit INT"  INT



# Parse command-line
-e while [[ -n $1 ]]; do
  case $1 in
-e     -h | --help)
      help_message; graceful_exit ;;
-ne     -a
-ne  | --adb
-ne )
      echo "The path to adb"
-ne ; shift; PATH="$1"
 ;;
-ne     -e
-ne  | --export
-ne )
      echo "> Set the location of the exported xls"
-ne ; shift; PATH="$1"
 ;;
-e     -* | --*)
      usage
-e       error_exit "Unknown option $1" ;;
-e     *)
      echo "Argument $1 to process..." ;;
-e   esac
  shift
done

# Main logic

graceful_exit


SEARCH_RESULT = $(./adb shell ls -lR | grep /ootbscouter)
echo ${SEARCH_RESULT}