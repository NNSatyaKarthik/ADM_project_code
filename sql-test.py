from sqlalchemy.ext.automap import automap_base
from sqlalchemy import create_engine
from sqlalchemy import MetaData
from sqlalchemy.sql import table, column, select, func, update, insert
from sqlalchemy import Table
from sqlalchemy.orm import Session

Base = automap_base()

engine = create_engine("sqlite:///database.sqlite")
filtered_engine = create_engine("sqlite:///filtered.db")
filtered_metadata = MetaData()
filtered_metadata.reflect(filtered_engine)

filtered_Base = automap_base(metadata=filtered_metadata)
filtered_Base.prepare()
Copied = filtered_Base.classes.copied
session = Session(engine)
filtered_session = Session(filtered_engine)

m = MetaData()
m.reflect(engine)

# from sqlalchemy import inspect
# inspector = inspect(engine)

# for table_name in inspector.get_table_names():
#     print "Table : ", table_name
#     for column in inspector.get_columns(table_name):
#         print("Column: %s" % column['name'])


# mapped classes are now created with names by default
# matching that of the table name.
# print Base.classes.copied
# for i in Base.classes :
#     print i

print "Old Database: "
for table in m.tables.values():
    print(table.name)
# for column in table.c:
#         print(column.name)

print "New Database: "
for table in filtered_metadata.tables.values():
    print table.name

copied_old = Table('copied', m, autoload=True)
May2015 = Table('May2015', m, autoload=True)
copied = Table('copied', filtered_metadata, autoload=True)


def getCount(table_name, engine):
    sc = select([func.count()]).select_from(table_name)
    session = Session(engine)
    return session.execute(sc)


def isValid(value):
    body = (value[17].encode('ascii', 'ignore')).strip()
    if body is "" or body is None:
        return False
    return True


def modify(value):
    #     l = []
    cols = ["created_utc", "ups", "subreddit_id", "link_id", "name", "score_hidden", "author_flair_css_class",
            "author_flair_text", "subreddit", "id", "removal_reason", "gilded", "downs", "archived", "author", "score",
            "retrieved_on", "body", "distinguished", "edited", "controversiality", "parent_id"]
    l = [i.encode('ascii', 'ignore') if isinstance(i, unicode) else i for i in value]
    d = {}

    for i in xrange(len(cols)):
        if l[i] is not None:
            d.__setitem__(cols[i], l[i])
        else:
            d.__setitem__(cols[i], 'None')

            #     for id in xrange(len(value)):
            #         i = value[id]
            #         if isinstance(i,  unicode):
            #             l.append("'"+str(i.encode('ascii', 'ignore'))+"'")
            #         else:
            #             l.append(str(i))
    return d


limitparam = 5000000
res = session.query(May2015).limit(limitparam)
# print res
from multiprocessing.dummy import Pool as ThreadPool
pool = ThreadPool(64)
res = pool.map(modify, [ i for i in res if isValid(i)])

# res = [modify(i) for i in res if isValid(i)]
# print res
count = 0;
bulkparam = 50000
l = []
ci = copied.insert()
for idx in xrange(len(res)):
#for idx in xrange(limitparam):
    i = res[idx]
#    print i
#    if not isValid(i):
#       continue
#    i = modify(res[idx])


#    print i, "\n\n\n\n"
    l.append(i)
    # filtered_session.add(d)
    if idx % bulkparam == 0:
        print len(l), count
        #         copied_new.insert(), l)
        filtered_engine.execute(Copied.__table__.insert(), l)
        l = []
        print 'inserted'
        count += 1
#    count+= 1
if len(l) != 0 :
    filtered_engine.execute(Copied.__table__.insert(), l)
