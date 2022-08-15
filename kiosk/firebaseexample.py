#-*-coding:utf-8 -*-

import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
from firebase_admin import db
from datetime import datetime

cred = credentials.Certificate("hypersonal-booster-firebase-adminsdk-ikwz1-f2efb4d5f9.json")
firebase_admin.initialize_app(cred,{'databaseURL': 'https://hypersonal-booster-default-rtdb.asia-southeast1.firebasedatabase.app/1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members'})
doc_ref = db.reference()

doc = doc_ref.get()
print(doc)
number = 0
userid = "test"
member_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/members"
booster_db = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster"
recommend_booster = ["1"]
recommend_booster.append(db.reference(member_db + "/" + str(userid)).get())
print(recommend_booster[1])
'''
booster_name = db.reference(booster_db + "/" + recommend_booster[1] + "/name").get()
booster_brand = db.reference(booster_db + "/" + recommend_booster[1] + "/brand").get()
booster_oneamount = db.reference(booster_db + "/" + recommend_booster[1] + "/amount").get()
booster_calories = db.reference(booster_db + "/" + recommend_booster[1] + "/calories(kcal)").get()
booster_carb = db.reference(booster_db + "/" + recommend_booster[1] + "/carb(g)").get()
booster_sugar = db.reference(booster_db + "/" + recommend_booster[1] + "/sugars(g)").get()
booster_fac = db.reference(booster_db + "/" + recommend_booster[1] + "/fat(g)").get()
booster_protein = db.reference(booster_db + "/" + recommend_booster[1] + "/protein(g)").get()
booster_taste = db.reference(booster_db + "/" + recommend_booster[1] + "/taste2").get()
'''
gram = 1
powtext = []
powtext = ["123"]
powtext.append("\n제품 : BCAA\n용량 : %dg\n상세성분: 1회분(30g)당 단백질 20g 탄수화물 10g" % gram)
print(powtext[0])
vender_powder = ["","1","2","3","4","5","6"]
print(vender_powder[2])
name = "마프웨이"
sort = "WPI"
brand = "마이프로틴"
taste = "딸기"
gram = "35"
oneamount = "30"
calories = "145"
carb = "12"
fat = "10"
protein = "23"
powtext_example = "\n제품 : %s\n보충제 종류 : %s\n브랜드 : %s\n맛 : %s\n추천제공량 : %s\n1회 제공량 %s당 칼로리%skcal,탄수화물 %sg, 지방 %sg, 단백질 %sg 함유"%(name, sort, brand, taste, gram,oneamount, calories, carb, fat, protein)
print(powtext_example)
'''
prot_db_link = "1RwUEzmqz5l9hilFIeJI5gEQu3AUwRAepCc4YzzJGnZY/booster/"
print(recommend_booster[1])
'''