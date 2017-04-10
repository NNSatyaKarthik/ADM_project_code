import sqlite3

conn = sqlite3.connect(database='/Volumes/My\\ Passport/Vivek\\ Data/reddit-comments-may-2015/database.sqlite')
command = "select id, author_flair_text, author_flair_css_class, body from May2015 limit 10;"
res = conn.execute(command)
for i in res:
    print i

conn.close()

