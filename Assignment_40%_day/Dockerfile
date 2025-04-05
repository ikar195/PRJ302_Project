# Dùng image Tomcat có sẵn Java
FROM tomcat:9.0-jdk17

# Xóa các webapp mặc định trong Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy file .war từ thư mục dist/ của NetBeans vào Tomcat
COPY dist/Assignment_40__day.war /usr/local/tomcat/webapps/ROOT.war

# Mở cổng 8080
EXPOSE 8080

# Chạy Tomcat
CMD ["catalina.sh", "run"]