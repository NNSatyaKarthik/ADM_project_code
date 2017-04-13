from sqlalchemy import Table
from sqlalchemy.ext.automap import automap_base
from sqlalchemy import create_engine
from sqlalchemy.orm import Session
from sqlalchemy.sql import table, column,func,  select, update, insert
from sqlalchemy import MetaData


class Copied_new:


Base = automap_base()

# engine, suppose it has two tables 'user' and 'address' set up
# engine = create_engine("sqlite:///chinook.db")
engine = create_engine("sqlite:///database.sqlite")
# reflect the tables
m = MetaData()
m.reflect(engine)

copied = Table('copied', m, autoload=True)
May2015 = Table('May2015', m, autoload=True)
def getCount(table_name):
    sc = select([func.count()]).select_from(table_name)
    session = Session(engine)
    return session.execute(sc)

# newTable = Table('copied_new')


print getCount(copied)
print getCount(May2015)


print copied.select().count
Base.prepare(engine, reflect=True)


# mapped classes are now created with names by default
# matching that of the table name.
for i in Base.classes:
    print i

# Employees = Base.classes.employees
# Customers = Base.classes.customers
#
# session = Session(engine)
#
#
# # rudimentary relationships are produced
# session.add(Address(email_address="foo@bar.com", user=User(name="foo")))
# session.commit()
