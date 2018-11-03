# 참고사항
1. 리눅스 환경

Java Version : openjdk 1.8.0_181

Javac Version : javac 1.8.0_181

Apache Tomcat Version : Apache Tomcat 8.0.32

Mysql Version : 15.1 Distrib 10.0.36-MariaDB

2. TODO_list.war파일 저장 디렉토리

/etc/var/tomcat8/webapps에 저장한 뒤 톰캣 실행하면 소스코드를 포함한 디렉토리가 생성됩니다. 톰캣 버전이 다르면 webapps 디렉토리를 찾아 그안에 저장하시면 됩니다.

3. Mysql TimeZone 설정

jdbc 드라이버로 DB와 연동 시 Mysql서버의 시간대와 맞추어야 하는데 서버의 시간대(time zone)가 UTC라고 가정하고 만들었지만 서버의 시간대가 KST라면 "TODOManager.java"코드 상의 DB와 연동하는 각 부분에 있는 "String myUrl = " 다음에 나오는 문자열의 끝에 있는 "UTC"를 "KST"로 수정해야합니다.


4. Firefox에서 TODO 삭제 시 오류

Firefox에서 실행 시 TODO를 삭제하는 과정에서 "삭제하시겠습니까?"라고 묻는 알림 창이 뜨는데 확인을 눌러도 페이지가 무한 새로고침 되는 현상이 있습니다. 이 문제는 해결하지 못했지만 Chrome과 Internet Explorer에서는 정상적으로 동작하므로 두 브라우저로 접속해주시면 됩니다.
