```C
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <pthread.h>

#define BUFFSIZE 100
#define NAMESIZE 20

int StoHEX(char fi, char sc);
void error_handling(char *msg);
void * send_message(void *arg);
void * recv_message(void *arg);

void * recv_message(void * arg){
    // 스레드 함수
    int sock = (int)arg;
    char buff[500];
    int len;
    while(1){
        len = read(sock,buff,sizeof(buff));
        if(len == -1){
            printf("sock close\n");
            break;
        }
        printf("%s", buff);
    }
    pthread_exit(0);
    return 0;
}

char message[BUFFSIZE];
int main(int argc, char**argv){
    int sock;
    struct sockaddr_in serv_addr;
    pthread_t rcv_thread;
    void* thread_result;
    char id[100];
    if(argc < 2){
        printf("You have to enter ID\n");
        return 0;
    }
    strcpy(id, argv[1]); // 실행시 받을 Id를 저장 (id배열)
    printf("id : %s\n", id); // id 출력
    sock = socket(PF_INET, SOCK_STREAM, 0);
    if(sock==-1){
        printf("socket() error");
    } else printf("socket is ready.\n");
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1");
    serv_addr.sin_port = htons(7989);
    if(connect(sock, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) == -1){
        printf("connect() error\n");
    } else printf("connect Success\n");

    pthread_create(&rcv_thread, NULL, recv_message, (void*)sock); // 메시지 수신을 위한 스레드 생성
    char chat[100];
    char msg[1000];
    while(1){
        gets(chat); // 채팅 받기
        sprintf(msg, "[%s] %s\n", id, chat); // msg 배열에 전송할 내용 저장
        printf("send : %s", msg);
        write(sock, msg, strlen(msg)+1); // NULL 문자 포함 위해 +1
        sleep(1);
    }
    close(sock);
    return 0;
} // end main
```