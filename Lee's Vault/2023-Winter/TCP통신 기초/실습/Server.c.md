```C
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>
// 스레드 사용할 것!

#define CLNT_MAX 10
#define BUFFSIZE 200

int g_clnt_socks[CLNT_MAX]; // 클라이언트 소켓 선언
int g_clnt_count = 0;

pthread_mutex_t g_mutex;

void sendAllClnt(char * msg, int my_sock){
    pthread_mutex_lock(&g_mutex);
    for(int i = 0; i < g_clnt_count; i++ ){
        if(g_clnt_socks[i] != my_sock){
            write(g_clnt_socks[i], msg, strlen(msg)+1);
        }
    }
    pthread_mutex_unlock(&g_mutex);
}

void* clnt_connection(void * arg){
    int clnt_sock = (int) arg;
    int str_len = 0;
    char msg[BUFFSIZE];
    int i;
    while(1){
        str_len = read(clnt_sock, msg, sizeof(msg));
        if(str_len == -1){ // 세션 끊어짐
            printf("clnt[%d] close\n", clnt_sock);
            break;
        }
        sendAllClnt(msg,clnt_sock);
        printf("%s\n", msg);
    }
    pthread_mutex_lock(&g_mutex);
    for(int i = 0; i<g_clnt_count; i++){
        if(clnt_sock == g_clnt_socks[i]){
            for(; i<g_clnt_count-1; i++){
                g_clnt_socks[i] = g_clnt_socks[i+1];
            }
        }
    }
    pthread_mutex_unlock(&g_mutex);
    close(clnt_sock);
    pthread_exit(0);
    return NULL;
}

int main(int argc, char** argv){
    int serv_sock; // 서버 소켓 하나 선언
    int clnt_sock; // 클라이언트 소켓 하나 선언 (63번째 줄)
    pthread_t t_thread;
    struct sockaddr_in clnt_addr; // 서버에 접속한 클라이언트의 IP(개인정보)를 받아냄.
    int clnt_addr_size;
    struct sockaddr_in serv_addr; // 서버 바인딩 용 구조체 하나 선언
    pthread_mutex_init(&g_mutex, NULL); // 스레드 뮤텍스를 위한 이닛
    
    serv_sock = socket(PF_INET,SOCK_STREAM, 0);

    //1. 소켓 만들기
    //IPv4를 이용할거야 (PF_INET)
    //TCP 통신을 할꺼야 (SOCK_STREAM)
    //UDP 통신을 할꺼야 (SOCK_DGRAM)
    //RAW 소켓을 쓸거야 (SOCK_RAW)
    //프로토콜은 앞에 Type에 맞춰서 쓸거야 (이번 경우엔 TCP)

    serv_addr.sin_family = AF_INET;

    //2. Bind 하기
    // 프로토콜 관련될 때는 PF, 주소 관련될 때는 AF
    // 이것도 IPv4를 쓰겠다는 말

    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);

    // 소켓 바인드 할때 주소값을 "내 지금 현재 PC의 IP"로 하겠다.
    // htonl = Host TO Network Long (호스트의 주소 오더 방식을 네트워크 오더로 바꿔라)
    // 네트워크 통신을 할때는 반드시 네트워크 오더(Big Endian)를 사용하자.
    //serv_addr.sin_port = htons(atoi(argv[1]));

    serv_addr.sin_port = htons(7989);
    // 포트 주소 넣기
    
    if(bind(serv_sock, (struct sockaddr * )&serv_addr, sizeof(serv_addr)) == -1){
        printf("Bind Error!");
    }
    // bind!

    if(listen(serv_sock, 5) == -1){
        printf("Listen Error!");
    }

    //3. Listen 하기
    // 서버소켓을 이용해 리슨, 접속 지연될 경우 대기할 수 있는 최대 클라이언트 수가 5개

    char buff[200];
    int recv_len = 0;

    while(1){
        clnt_addr_size = sizeof(clnt_addr);
        clnt_sock = accept(serv_sock, (struct sockaddr*)&clnt_addr, &clnt_addr_size);

        // 서버소켓과 클라이언트 소켓이 있다.
        // 클라이언트 소켓은 클라이언트 한명당 하나. 얘가 실질적으로 통신하는 역할을 함.
        // 서버소켓은 Listen 하고 있음.
        // 처음에는 서버소켓을 이용해 connection을 하고, 이가 끝나면 클라이언트 소켓을 이용해 패킷을 주고받음.
        
        pthread_mutex_lock(&g_mutex);
        g_clnt_socks[g_clnt_count++] = clnt_sock;

        // 클라이언트 소켓 리스트에 클라이언트 소켓을 넣고 카운트 하나 추가
        // 임계영역이기 때문에 lock 걸어줄 필요가 있음.

        pthread_mutex_unlock(&g_mutex);
        pthread_create(&t_thread, NULL, clnt_connection, (void *)clnt_sock);

        // 읽기전용 스레드 만듬 - clnt_connection이라는 함수 실행, clnt_sock 인자 전달
    }

}
```

# 주의사항
```bash
gcc -o servertest server.c -lpthread
```
스레드를 이용할 때는 `-lpthread` 라이브러리를 포함해야한다.