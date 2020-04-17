
        exec {'apt-update':
                command => '/usr/bin/apt-get update'
        }
 
        package{'docker.io':
                ensure => present
        }
