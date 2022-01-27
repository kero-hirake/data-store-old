## todo
- [ ] データと測定日時を書き込み
  - [ ] 測定日時は、日付文字列
  - [ ] 測定日時は、1970/1/1からのミリ秒

### 重要度、テスト容易性 high high


### 重要度、テスト容易性 los low


## memo
ambientの仕様
- データ
  - 書き込み
    - データだけ書き込み
    - データと測定日時を書き込み
    - 複数日時のデータを書き込み
    - 1チャネルあたり8種類のデーターを書き込み。
    - 送信から次の送信まではチャネルごとに最低5秒空ける必要があります。それより短い間隔で送信したものは無視されます。
    - ~~1チャネルあたり1日3,000件までデーターを登録できます。平均すると28.8秒に1回のペースです。~~
    - ~~件数のカウントは0時に0クリアされます。~~
    - ~~チャネルデーターを削除しても1日の登録件数のカウントは0クリアされません。~~
  - 読み込み
    - 件数を指定して読み込み
    - 日時を指定して読み込み
    - 期間を指定して読み込み
    - 1チャネルあたり8個までチャートを生成できます。
  - 削除
    - 
- チャンネル
  - 作成
    - 1ユーザーあたり8個までチャネルを生成できます。
  - 取得
  - 削除

- ~~データーの保存期間は1年間です。~~
  

****************************************************************************************

    bool Ambient::begin(unsigned int channelId, const char * writeKey, WiFiClient * c[, const char * readKey]);
書き込み
    am = ambient.Ambient(チャネルId, ライトキー[, リードキー[, ユーザーキー]])
    r = am.send({'d1': 数値, 'd2': 数値}[, timeout = timeout])

    データーを測定した時刻を指定することもできます。
    r = am.send({'created': 'YYYY-MM-DD HH:mm:ss.sss', 'd1': 1.1, 'd2': 2.2})

    複数のデーターを一括で送信することもできます。
    data = [
        {'created': '2017-02-18 12:00:00', 'd1': 1.1, 'd2': 2.1},
        {'created': '2017-02-18 12:01:00', 'd1': 1.5, 'd2': 3.8},
        {'created': '2017-02-18 12:02:00', 'd1': 1.0, 'd2': 0.8}
    ]
    r = am.send(data)
    送信に成功していれば200
読み込み
    件数指定
    d = am.read(n=件数[, skip=スキップ件数[, timeout = timeout]])
    日時指定
    d = am.read(date='YYYY-mm-dd'[, timeout = timeout])
    期間指定
    d = am.read(start='YYYY-mm-dd HH:MM:SS', end='YYYY-mm-dd HH:MM:SS'[, timeout = timeout])



複数回分のデーターをまとめてAmbientに送信する機能です。
 戻り値は実際に送信できたバイト数です
   データーは次のようなJSONフォーマットで渡します。

    {
        “writeKey” :  “ライトキー”,
        “data” : [
            {“created” : “YYYY-MM-DD HH:mm:ss.sss”, “d1” :  “値”, “d2” :  “値”, ...},
            {“created” : “YYYY-MM-DD HH:mm:ss.sss”, “d1” :  “値”, “d2” :  “値”, ...},
            ...
            {“created” : “YYYY-MM-DD HH:mm:ss.sss”, “d1” :  “値”, “d2” :  “値”, ...}
        ]
    }

     “created”はデーターの生成時刻で、値は“YYYY-MM-DD HH:mm:ss.sss”という形式か、 数値を渡します。 数値を渡した場合は1970年1月1日00:00:00からのミリ秒と解釈されます。 マイコンなどではミリ秒で扱うのが楽だと思います。 “created”の後にはデーターを”キー”:”値”という形式で並べます。 キーは”d1″、”d2″、”d3″、”d4″、”d5″、”d6″、”d7″、”d8″のいずれかを指定します。

      ただし、send()はデーターを受信した時にAmbientが時刻を記録するので、 マイコン側では時刻を管理する必要がありませんが、bulk_send()では マイコン側で時刻を管理し、データーに時刻を含めて送信する必要があります。

      指定したチャネルIDのチャネルに保存されたデーターを削除します。
      チャネルに保存されたデーターを全て削除します.
      チャネルそのものやチャネルの設定情報は削除されずに残ります。

       send()、bulk_send()
       JSONデーターに含まれる時刻とデーターの組み{“created” : “時刻”, データー…}を1件と数えます。
       最大累積件数を超えて登録すると、最大累積件数まではデーターが登録され、 それ以上のデーターは無視されて、エラー(413)が返されます。
       累積件数を計算するときのデーターの日付は、apiをコールした日付が使われます。 データーに付与された日付ではありません。