package com.maraquya.virtualization

class Docker implements Serializable {
    private script

    Docker(script) {
        this.script = script
    }

    void run(String image, String command, String flags = '') {
        this.script.sh("docker run --rm --user=\"\$(id -u):\$(id -g)\" ${flags} ${image} ${command}")
    }
}
