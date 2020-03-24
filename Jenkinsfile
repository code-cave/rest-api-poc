@Library('some-handy-library@master')

String repoName = 'rest-api-poc'
// The WORKSPACE_DIR var is mandatory
String WORKSPACE_DIR = workspace([ default: true ])
// Git vars
boolean MASTER = env.BRANCH_NAME == 'master'
boolean RELEASE
// Required Map
Map requiredVars = [ REPO_NAME: repoName, REPO_ORG: 'ORG', WORKSPACE_DIR: WORKSPACE_DIR ]
// Unit test variables
String unitTestResultsFile = '**/surefire-reports/*Spec.xml'
Map unitTestMap = [ testsDir: 'target/surefire-reports', matchStr: '*Spec.xml', unitTestFile: './unit-test-results.xml' ]
// Sonar variables
Map sonarMap = [ reportFile: 'target/sonar/report-task.txt', branch: env.BRANCH_NAME ]
// Docker variables
Map nonprodDockerMap = [ push: 'nonprod' ]
Map prodDockerMap = [ push: 'prod' ]
// OpenShift variables
String nonprodDomain = 'https://domain-nonprod.com:443'
String prodDomain = 'https://domain.com:443'
Map nonprodOCMap = [ ocURL: nonprodDomain, ocProject: "nonprod-${repoName}", yamlFile: 'deploy/my-retail-api-nonprod.yml', delete: true ]
Map prodOCMap = [ ocURL: prodDomain, ocProject: "rod-${repoName}", yamlFile: 'deploy/my-retail-api-prod.yml', delete: true, prod: true ]

// Properties for the pipeline
def props = [ buildDiscarder(logRotator(numToKeepStr: '25')), disableConcurrentBuilds() ]
// Pipeline script
node { ws (WORKSPACE_DIR) { properties(props)
// The Map below must get declared from within the workspace
Map vars = pipelineVariables.create(requiredVars)
// Wrap with the withCredentials
withCredentials(vars.CREDENTIALS) {
try {
    // Should ALWAYS set the Jenkins environment path
    env.PATH = vars.PATH
    RELEASE = isRelease(vars, [:])
    // Run commit check against Pull Requests
    stage('Commit Message Check') { if (env.CHANGE_TARGET) { commitCheck(vars.STATUS_MAP) } }
    // Pull request checks
    stage('Code Checks') {
         stage('Unit Tests') { unitTest(vars, unitTestMap); junit testResults: unitTestResultsFile }
         stage('SonarQube Code Scan'): { sonarScan(vars, sonarMap) }
    }
    stage('Maven Install') { sh 'mvn install -DskipTests' }
    stage('Nonprod Build') {
        if (MASTER) {
            stage('Build Nonprod Dockerfile') { buildDockerfile(vars, nonprodDockerMap) }
            stage('Deploy to Nonprod OpenShift') { createOCApp(vars, nonprodOCMap) }
        }
    }
    stage('Prod Build') {
        if (RELEASE) {
            stage('Build Prod Dockerfile') { buildDockerfile(vars, prodDockerMap) }
            stage('Deploy to Prod OpenShift') { createOCApp(vars, prodOCMap) }
        }
    }
}
catch (e) { currentBuild.result = 'FAILURE'; if (MASTER || RELEASE) { sendExtEmail(vars.EMAIL) }; throw e }
finally { try { resolveStatuses(vars) } catch (e) { currentBuild.result = 'FAILURE' } }
}}}
