import sqlite3
import numpy as np
import unicodedata

# conn = sqlite3.connect(database='/Volumes/My\\ Passport/Vivek\\ Data/reddit-comments-may-2015/database.sqlite')
conn = sqlite3.connect("database.sqlite")
# destination = sqlite3.connect("database_1M_eng.sqlite")
command = "select *  from May2015;"

res = conn.execute(command)
coutn = 10


def transfrm(args):
    if args is str:
        return "'"+str(args)+"'"
    return args


def write_toDb(string):
    # comment = np.array(string)
    print string
#    print map(transfrm, string)
    cmd = "insert into copied (created_utc ,  ups,  subreddit_id,  link_id,  name,  score_hidden,  author_flair_css_class,  author_flair_text,  subreddit,  id,  removal_reason,  gilded,  downs,  archived,  author,  score,  retrieved_on,  body,  distinguished,  edited,  controversiality,  parent_id) VALUES (";
    for id in xrange(len(string)):
        i = string[id]

        if isinstance(i,  unicode):
            cmd += "'"+str(i.encode('ascii', 'ignore'))+"'"
        else:
            cmd += str(i)
        if id is not len(string)-1:
            cmd += ", "
    cmd += ");"
    print cmd
    # conn.execute(cmd)
    pass




def isValid(value):
    body = (value[17].encode('ascii', 'ignore')).strip()
    if body is "" or body is None:
        return False
    return True


for i in res:
    if coutn < 0: break;
    body = (i[17].encode('ascii', 'ignore')).strip()
    # print "original: ", i[0], "Encoded: ", str
    if body is ""  or body is None :
        print ".."
    else:
        write_toDb(i)
        coutn -= 1
    conn.commit()


# destination.close()
conn.close()