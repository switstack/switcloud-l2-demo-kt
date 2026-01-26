# Commands
AWK ?= awk
ECHO ?= echo
GET_NEXT_VERSION ?= get-next-version
GRADLE ?= ./gradlew
GREP ?= grep
MKDIR ?= mkdir
MV ?= mv
RM ?= rm
SORT ?= sort

UNAME := $(shell uname)
ifeq ($(UNAME), Darwin)
	SED := sed -i ''
else
	SED := sed -i
endif

PROJECT = switcloud-l2-demo

default: help

.PHONY: help
help:
	@$(GREP) -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | $(SORT) | $(AWK) 'BEGIN {FS = ":.*?## "}; \
	{printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

###########
# INSTALL #
###########

.PHONY: install
install: ## Install dependencies

#########
# Build #
#########

.PHONY: clean
clean: ## Clean build
	$(GRADLE) -q clean

# ! build-and-publish CI action
.PHONY: build-artifacts
build-artifact: ## Build project artifacts (AAR packages)
	$(GRADLE) -q assemble

# ! build-and-publish CI action
.PHONY: build-doc
build-doc: ## Build project documentation (MKDocs and KDoc)

#############
# CODESTYLE #
#############

.PHONY: run-code-style-fix
run-code-style-fix: ## Fix code style
	$(GRADLE) -q ktlintFormat

.PHONY: code-style-check
run-code-style-check: ## Check code style (no fix)
	$(GRADLE) -q ktlintCheck

#################
# CODE ANALYSIS #
#################

.PHONY: run-code-analysis-fix
run-code-analysis-fix: ## Run code analysis
	$(GRADLE) -q detekt --auto-correct

.PHONY: run-code-analysis
run-code-analysis: ## Run code analysis
	$(GRADLE) -q detekt

#########
# TESTS #
#########

.PHONY: run-coverage-test
run-coverage-test: ## Check test coverage

.PHONY: tests-all
tests-all: tests-ut tests-inte tests-func tests-smoke ## Run all tests

.PHONY: run-ut-tests
run-ut-tests: ## Run unit tests

.PHONY: run-inte-tests
run-inte-tests: ## Run integration tests

.PHONY: run-func-tests
run-func-tests: ## Run functionnal tests

.PHONY: run-ci-tests
run-ci-tests: ## Run CI tests

.PHONY: run-manual-tests
run-all-tests: ## Run all tests

###########
# PUBLISH #
###########

.PHONY: publish-artifact
publish-artifact: ## Publish main artifact

#############
# LIFECYCLE #
#############

.PHONY: set-version
set-version: ## Version update
	@version=$$($(GET_NEXT_VERSION)); \
	$(SED) -E "s/(versionName = )\".*\"/\1\"$$version\"/" $(PROJECT)/build.gradle.kts

.PHONY: get-version
get-version: ## Read current version
	@version=$$(grep "versionName =" $(PROJECT)/build.gradle.kts | sed -E 's/.*versionName = "(.*)"/\1/'); \
    $(ECHO) "$$version"
