default: help

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; \
	{printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

###########
# INSTALL #
###########

.PHONY: install
install: ## Install dependencies

.PHONY: generate
generate: ## Generate code (openapi based projects only)
	
#########
# BUILD #
#########
	
.PHONY: build
build-artifact: ## Build package or lib

.PHONY: doc-build
build-doc: ## Build API documentation
	
#############
# CODESTYLE #
#############

.PHONY: code-style-fix
code-style-fix: ## Fix code style

.PHONY: code-style-check
code-style-check: ## Check code style (no fix)

#########
# TESTS #
#########

.PHONY: tests-coverage
tests-coverage: ## Check test coverage

.PHONY: tests-perf
tests-perf: ## Run performance tests

.PHONY: tests-migration
tests-migration: ## Run migration tests

.PHONY: tests-sql-backup
tests-sql-backup: ## Run tests against SQL backup

.PHONY: tests-ut
tests-ut: ## Run unit tests

.PHONY: tests-inte
tests-inte: ## Run integration tests

.PHONY: tests-func
tests-func: ## Run functionnal tests

.PHONY: tests
tests: ## Run all tests
  make tests-coverage
  make tests-perf
  make tests-migration
  make tests-sql-backup
  make tests-ut && make tests-inte && make tests-func

###########
# PUBLISH #
###########

.PHONY: publish-artifact
publish-artifact: ## Publish main artifact

.PHONY: publish-doc
publish-doc: ## Publish API documentation

##########
# DEPLOY #
##########

.PHONY: deploy
deploy: ## Deploy main artifact

#############
# LIFECYCLE #
#############

.PHONY: bump
bump: ## Version bump

