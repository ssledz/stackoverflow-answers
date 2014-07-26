#!/bin/bash
# How to replace full column with the last value?
# http://stackoverflow.com/questions/24970523/how-to-replace-full-column-with-the-last-value/24971282#24971282
var=`tail -1 sample.csv | perl -ne 'm/([^,]+)$/; print "$1";'`; cat sample.csv | while read line; do echo $line | perl -ne "s/[^,]*$/$var\n/; print $_;"; done
