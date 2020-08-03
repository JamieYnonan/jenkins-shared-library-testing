.DEFAULT_GOAL := help

LOCAL_UID = "$(shell id -u)"
LOCAL_GID = "$(shell id -g)"
LOCAL_HOME = $$HOME

TIME_ZONE ?= America/Lima

CLI_IMAGE = maven
CLI_VERSION = 3.6-jdk-11-openj9

mvn: ## Execute mvn: make mvn EXTRA_FLAGS="-e ENV_NAME=ENV_VALUE" COMMAND="package -Dmaven.test.skip=true"
	docker run --rm -it --name shared-libray-testing-cli \
		-u ${LOCAL_UID}:${LOCAL_GID} \
		-v $$PWD:/usr/src/app \
		-v ${LOCAL_HOME}/.m2/:/var/maven/.m2/ \
		-e MAVEN_CONFIG=/var/maven/.m2 \
		-w /usr/src/app ${EXTRA_FLAGS} \
		${CLI_IMAGE}:${CLI_VERSION} sh -c "mvn -Duser.home=/var/maven ${COMMAND}"

execute-test: ## Execute tests: make execute-test
	$(MAKE) mvn COMMAND="clean test"

## Help ##
help:
	@printf "\033[31m%-25s %-50s %s\033[0m\n" "Target" "Help" "Usage"; \
	printf "\033[31m%-25s %-50s %s\033[0m\n" "------" "----" "-----"; \
	grep -hE '^\S+:.*## .*$$' $(MAKEFILE_LIST) | sed -e 's/:.*##\s*/:/' | sort | awk 'BEGIN {FS = ":"}; {printf "\033[32m%-25s\033[0m %-49s \033[34m%s\033[0m\n", $$1, $$2, $$3}'