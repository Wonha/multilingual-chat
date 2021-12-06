# Multilingual Chat

1. Chat group id を生成する。
```sh
curl --location --request POST 'http://${SERVER_ADDRESS}/chats/groups' \
--header 'Content-Type: application/x-www-form-urlencoded' \
--data-urlencode 'name=My Test Room'
```

2. サーバーへ WebSocket プロトコルで接続する。  
`ws://${SERVER_ADDRESS}/ws/chat`

3. 入りたい Chat group に入場する。
```json
{
	"type": "ENTER",
	"text": "",
	"sender": {
		"name": "wonha",
        "lang": "ko"
	},
	"groupId": "${GROUP_ID}"
}
```

4. 好きな言語で会話する。
```json
{
	"type": "TALK",
	"text": "안녕하세요",
	"sender": {
		"name": "Wonha",
        "lang": "ko"
	},
	"groupId": "${GROUP_ID"
}
```
