# -*- coding: utf-8 -*-
"""
Created on Wed Sep 28 10:22:54 2022

@author: HP
"""
import streamlit as st
import pickle
from PIL import Image

st.set_page_config(page_title='House Rent Predictor')
image = Image.open('House_Image.jpg')
st.image(image, "Mansion")
st.title('House Rent Predictor')

encoded_values = pickle.load(open("encoded_values.pkl", 'rb'))

inputs = { 'type':'House Type', 'locality':'In which Locality', 
           'lease_type':'Lease Type of House', 'negotiable':'Is Price Negotiable?', 
           'furnishing':'Is House Furnished', 'parking':'Parking Facility is Available?', 
           'property_size':'Property Size', 'property_age':'Property Age', 
           'bathroom':'Number of Bathroom', 'facing':'Facing in which Direction',
           'cup_board':'Number of Cup Boards', 'floor':'Number of Floors', 
           'total_floor':'Total Number of Floors', 
           'water_supply':'Water Supply Facility is Available?', 
           'building_type':'Building Type',
           'balconies':'Number of Balconies', 
           'LIFT':'Is Lift Facility Available?', 'GYM':'Is Gym Facility Available?', 
           'INTERNET':'Is Internet Facility Available?', 
           'AC':'Is Air Conditioner(AC) Facility Available?', 
           'CLUB':'Is Club Available?', 'INTERCOM':"Is Intercom Facility Available?", 
           'POOL':'Is Swimming Pool Available?', 'CPA':"Is College Park Academy(CPA) Available?", 
           'FS':"Is Financial Services(FS) Available?", 'SERVANT':"Are Servants Available?", 
           'SECURITY':"Is Security Available?", 'SC':"Is SC Available?", 'GP':"Is GP Available?",
           'PARK':"Is Park Available?", 'RWH':"Is Rain Water Harvesting(RWH) there?", 
           'STP':"Is Sewage Treatment Plant(STP) is Available?", 'HK':'Is HK Available?', 
           'PB':"Is PB Available?", 'VP':"Is VP Available?", 'Suburb':"Suburban", 
           'City_district':"City District", 'City':"In which City?", 
           'County':"Select Country and City", 'State_district':"Select the State District", 
           'State':"Select State", 'Postcode':"Select or Enter Postcode" }
encoded_values['negotiable'] = ['No', 'Yes']
features = []
values = {'property_size' : [100, 5000], 'property_age' : [0, 400], 
          'bathroom' : [1, 7], 'cup_board' : [0, 40], 'floor' : [0, 25], 
          'total_floor' : [0, 26], 'balconies' : [0, 10]}

model = pickle.load(open('Model_high_250_20.sav', 'rb'))
#model = pickle.load(open('Model_mid.sav', 'rb'))

for item in inputs.keys():
    st.subheader(inputs[item])
    if item in encoded_values.keys():
        gen_display = encoded_values[item]
        gen_display.append("Select")
        #print(gen_display)
        gen_options = list(range(len(gen_display)))
        #print(gen_options)
        gen = st.selectbox("Select an Option", gen_options, key=item, 
                           index=len(gen_options)-1, format_func=lambda x: gen_display[x])
        if gen == len(gen_options)-1 :
            gen = 0
        features.append(gen)
        st.write("The " + item + " is : " + str(gen_display[gen]))
        
    else:
        lb = values[item][0]
        ub = values[item][1]
        number = st.select_slider("Choose a Number", options=range(lb, ub+1))
        features.append(number)
        st.write("The " + item + " is : " + str(number))
        
    st.markdown('''---''')

if st.button("Submit"):
    print(features)
    result = model.predict([features])
    st.success("The Predicted Rent of the House is :  Rs. " + str(result[0]))
    
    
    