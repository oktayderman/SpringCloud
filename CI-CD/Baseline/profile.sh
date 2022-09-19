#setting prompt
#. $FCBS_ROOT/config/configProfile.sh
export PS1="\u@\h:[\w]$ "


alias ll="ls -lart"
alias tt="tail -1000f nohup.out;clear;"
alias psb="cd $FCBS_ROOT/bin/"
alias psc="cd $FCBS_ROOT/config/"
alias psl="cd $FCBS_ROOT/lib/"
alias start="(start*.sh &)"
alias stop="(stop*.sh)"
alias startt="(start) ; tt"
alias psf="ps -edf | grep BARONS | grep $USER | grep -v grep | cut -c 1-150"
#create new link
#refresh profile.sh
alias rp=". $FCBS_ROOT/profile.sh"




