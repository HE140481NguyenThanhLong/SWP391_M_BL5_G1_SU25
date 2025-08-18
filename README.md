nhét cái này vào file application.yaml để chạy được cái gửi mail. gửi mail cũng có thể sử dụng trong các flow khác, gọi là đc.
Tài khoản gg gmail là smartshopforeveryone@gmail.com meomeo2589143 
Cần 2 đăng nhập 2 lớp để có cái password dưới nên đừng hỏi tại sao, muốn nghịch thì call
  # SMTP Configuration for Gmail
  mail:
    host: smtp.gmail.com
    port: 587
    username: smartshopforeveryone@gmail.com
    password: ynzx kxzg upzh jhvk
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            trust: smtp.gmail.com
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
