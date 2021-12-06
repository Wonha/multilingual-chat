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
		"name": "白井",
        "lang": "ja"
	},
	"groupId": "${GROUP_ID}"
}
```

4. 好きな言語で会話する。
```json
{
	"type": "TALK",
	"text": "こんにちは。白井です。",
	"sender": {
		"name": "白井",
        "lang": "ja"
	},
	"groupId": "${GROUP_ID}"
}
```

レスポンスの例（ユーザーの言語選択が en の場合）：
```json
[
  {
    "id": "945dc3ee",
    "num": 3,
    "original": null,
    "lang": "ja",
    "text": "こんにちは",
    "type": "TALK",
    "createdAt": "2021-12-05T16:22:21.533246Z",
    "sender": {
      "name": "Shirai"
    },
    "translation": [
      {
        "lang": "en",
        "text": "Hi, I am Shirai"
      }
    ]
  }
]
```