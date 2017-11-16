#!/bin/sh
xterm -hold -title "Router 1" -e "python daemon.py config_1" &
xterm -hold -title "Router 2" -e "python daemon.py config_2" &
xterm -hold -title "Router 3" -e "python daemon.py config_3" &
xterm -hold -title "Router 4" -e "python daemon.py config_4" &
xterm -hold -title "Router 5" -e "python daemon.py config_5" &
xterm -hold -title "Router 6" -e "python daemon.py config_6" &
xterm -hold -title "Router 7" -e "python daemon.py config_7" &
xterm -hold -title "Reserve Router 1" &
xterm -hold -title "Reserve Router 2" &