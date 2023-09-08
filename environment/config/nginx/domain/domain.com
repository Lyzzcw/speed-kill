server {
    listen       11111;
    server_name  localhost;
    location / {
        root   html;
        index  index.html index.htm;
    }
    location ^~ /speedkill {
        #limit_req zone=limit_by_ip burst=1 nodelay;
        limit_req zone=limit_by_user burst=1 nodelay;
        proxy_pass http://real_server;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
    error_page   500 502 503 504  /503_fail.html;
    location = /50x.html {
        root   html;
    }
    #模拟用户id
    set_by_lua_block $user_id{
        return "lzy";
    }
}
server {
    listen       11112;
    server_name  localhost;
    location /hello {
        default_type text/plain;
        content_by_lua_block {
            ngx.say("{\"response\" : \"hello world!!!\"}")
        }
    }
}