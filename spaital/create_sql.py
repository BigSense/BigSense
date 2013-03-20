#!/usr/bin/env python3

import os

fd = open('Sensor Reference.csv','r')
lines = fd.readlines()
fd.close

for i in lines:
  fields = i.split(',')
  if fields[8] != "":
    point = "INSERT INTO sensor_locations VALUES('{0}',geography::STGeomFromText('POINT({1} {2})',4326));".format(
    fields[8],fields[9],fields[10])
    print(point)
