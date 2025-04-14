```C
#include <pthread.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>

int a = 0;
void * thread1(void * arg){
    printf("arg : %d\n", (int)arg);
    while(1){
        printf("thread1 : a[%d]\n", ++a);
        sleep(2);
    }
    return NULL;
}

int main(){
    pthread_t s_thread;
    int b = 77;
    pthread_create(&s_thread, NULL, thread1, (void*)b);
    pthread_join(s_thread, NULL);
}
```

# 특이사항
```bash
gcc thread.c -lpthread
```
- 컴파일 할 때 pthread 라이브러리를 참조해야 함. 