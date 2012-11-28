Piraso [![Build Status](https://buildhive.cloudbees.com/job/alvinrdeleon/job/piraso/badge/icon)](https://buildhive.cloudbees.com/job/alvinrdeleon/job/piraso/)
=====

### Introduction

Development tool for debugging and analyzing request context log information captured from a supported java module.

To learn more please see https://github.com/alvinrdeleon/piraso/wiki.

### Developer's Notes

1. Command to create package snapshots
```
    mvn -DaltDeploymentRepository=snapshot-repo::default::file:../piraso-mvn-repo/snapshots clean deploy
```

2. Deploying third party jars.
```
    mvn deploy:deploy-file -DgroupId={groupId} -DartifactId={artifactId} -Dversion={version} -Dpackaging=jar -Dfile={file}.jar -Durl=file:///{piraso.home}/piraso-mvn-repo/releases/
    mvn deploy:deploy-file -DgroupId={groupId} -DartifactId={artifactId} -Dversion={version} -Dpackaging=java-source -DgeneratePom=false -Dfile={file}-sources.jar -Durl=file:///{piraso.home}/piraso-mvn-repo/releases/
```

