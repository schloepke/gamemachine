export DEBIAN_FRONTEND=noninteractive

if [ -f "/home/vagrant/.packages_installed" ]
then
  echo "packages already installed"
else
  apt-get update
  apt-get install -y mysql-server
  apt-get install -y mysql-client
  apt-get install -y libmysqlclient-dev
  apt-get install -y openjdk-7-jdk
  apt-get install -y git-core
  apt-get install -y curl
  apt-get install -y unzip
  touch .packages_installed
fi

# Install rvm and jruby
if [ -d "/usr/local/rvm" ]
then
  echo "rvm already installed"
else
  curl -L https://get.rvm.io | bash -s stable
  source /etc/profile.d/rvm.sh
  rvm install jruby
  usermod -a -G rvm vagrant
fi

# Install game machine
if [ -d "/home/vagrant/game_machine" ]
then
  echo "game_machine already installed"
else
  echo "installing game machine"
  wget https://github.com/chrisochs/game_machine/archive/master.zip
  unzip master.zip
  mv game_machine-master game_machine
  rm master.zip
  #git clone https://github.com/chrisochs/game_machine
fi

echo "Game Machine install successful!"
