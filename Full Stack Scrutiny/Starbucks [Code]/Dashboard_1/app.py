import pandas as pd
import streamlit as st
import plotly.express as px
import matplotlib.pyplot as plt
import plotly.graph_objects as go
import seaborn as sns
from streamlit_option_menu import option_menu
from sklearn.metrics import confusion_matrix , classification_report

#st.set_page_config(page_title='STARBUCKS ANALYSIS')
#st.subheader('Was the tutorial helpful?')
#Changes Starts Here

st.set_option('deprecation.showPyplotGlobalUse', False)


st.set_page_config(page_title="Starbucks Analysis", page_icon=":bar_chart:", layout="wide")
#st.header('STARBUCKS ANALYSIS')

# Changes ends here

selected = option_menu(
        menu_title="STARBUCKS ANALYSIS",
        options=["Customer Segmentation", "Sentiment Analysis", "Behaviour Analysis"],
        icons=["clipboard-data", "emoji-laughing", "bar-chart-steps"],
        menu_icon="cast",
        default_index=0,
        orientation="horizontal",
        )
    
if selected == "Customer Segmentation":
    st.title("Customer Segmentation")
    
    st.subheader('Age Distribution')
    profile_1 = pd.read_csv('./profile_1.csv')
    
    fig_1 = px.bar(profile_1, x='age_range', y='no_of_cust',
                 color = 'age_range',
                 labels={'no_of_cust':'Number of Customers', 'age_range' : 'Age Range'}, title='Age Distribution of Starbucks Customers',
                 width=1200, height=800)
    
    st.plotly_chart(fig_1)
    
    st.subheader('What are the salary ranges of people across different age groups?')
    
    profile_2 = pd.read_csv('./profile_2.csv')
    fig_2 = px.violin(profile_2, x="age_range", y="income", color="age_range", box=True, title='Starbucks Income Distribution across age Ranges', labels={'age_range' : 'Age Ranges', 'income' : 'Income'}, width=1200, height=700)
    #fig = px.violin(profile, y="income", x="age_range", color="age_range", box=True, points='all')
    st.plotly_chart(fig_2)
    
    fig_3 = px.scatter(profile_2, x="age", y="income", color='age', title='Starbucks Customers - Income Distribution Across Individual Ages', labels={'age' : 'Age', 'income' : 'Income'}, height=700, width=1200)
    st.plotly_chart(fig_3)
    
    transcript_amount = pd.read_csv('./transcript_amount.csv')
    st.subheader('What does the correlation between number of days an offer has been open vs. final transaction amount have to tell us?')
    fig_4 = px.scatter(transcript_amount, x="days", y="amount", color='days', title='Days Reward Open vs. Final Amount Spent', labels={'days' : 'Days', 'amount' : 'Amount'}, height=800, width=1200)
    #st.plotly_chart(fig_4)
    
    fig_5 = px.scatter(transcript_amount, x="days", y="amount", color='days', title='Days Reward Open vs. Final Amount Spent' , labels={'days' : 'Days', 'amount' : 'Amount'}, range_y=(0,50), height=800, width=1200)
    #st.plotly_chart(fig_5)
    
    fig_6 = px.histogram(profile_2.sort_values(by='age_range'), x='age_range', 
                 color='gender', barmode='group',
                 height=800, width=1200, title='Distribution of Genders Across Age Ranges', labels={'age_range' : 'Age Ranges', 'count' : 'Number of Peoples'})
    st.plotly_chart(fig_6)
    
    st.subheader("Distribution of Income Across Genders and Ages")
    
    fig_7 = plt.figure(figsize=(15,10))
    sns.barplot(data = profile_2, x = 'age_range', y = 'income', hue = 'gender', palette='cool_r')
    fig_7 = plt.xlabel('Age Ranges')
    fig_7 = plt.ylabel('Income')
    fig_7 = plt.style.use('seaborn')
    st.pyplot(fig_7)
    
    st.subheader('Number of Clusters using K-Means')
    k_means_result = pd.read_csv('./K_means_result.csv')
    fig = px.line(k_means_result, x='no_of_clusters', y='sum_of_squared_error', markers=True, height=800, width=1200, title=('K Means SSE Scores as K Increases'))
    fig.update_layout(title='KMeans SSE Scores as K Increases',
                       yaxis_title='SSE',
                       xaxis_title='Number of Clusters (K)')
    st.plotly_chart(fig)
    
    st.subheader('Scattered Plot for each and Every Cluster')
    cust_tran_master_1 = pd.read_csv('./cust_tran_master_1.csv')
    fig = px.scatter(cust_tran_master_1, y="income", x="age", color="cluster_1", symbol="cluster_1", render_mode='webgl', height=800, width=1200, labels=({'age' : 'Age', 'income' : 'Income', 'cluster_1' : 'Different Clusters (k = 5)'}))
    fig.update_layout(legend_orientation="h")
    st.plotly_chart(fig)
    
    st.subheader('Number of Customers in a Particular Cluster')
    fig = px.histogram(cust_tran_master_1, x="cluster_1", labels=({'cluster_1' : 'Cluster Number', 'count' : 'Count'}), height=800, width=1200)
    fig.update_layout(bargap=0.2)
    st.plotly_chart(fig)
    
    fig = px.pie(cust_tran_master_1, cust_tran_master_1['cluster_1'],color_discrete_sequence=px.colors.sequential.PuBuGn_r,height=700, width=1200)
    st.plotly_chart(fig)
    
    st.subheader('Clustering between Age and Gender')
    k_means_result_2 = pd.read_csv('./K_means_result_2.csv')
    fig = px.line(k_means_result_2, x='Number_of_Clusters', y='K_Means_Score', markers=True, height=800, width=1200, title=('K Means SSE Scores as K Increases'))
    fig.update_layout(title='KMeans SSE Scores as K Increases',
                       yaxis_title='SSE',
                       xaxis_title='Number of Clusters (K)')
    st.plotly_chart(fig)
    
    st.subheader('Clustering between Age and Income')
    k_means_result_3 = pd.read_csv('./K_means_result_3.csv')
    fig = px.line(k_means_result_3, x='Number_of_Clusters', y='K_Means_Score', markers=True, height=800, width=1200, title=('K Means SSE Scores as K Increases'))
    fig.update_layout(title='KMeans SSE Scores as K Increases',
                       yaxis_title='SSE',
                       xaxis_title='Number of Clusters (K)')
    st.plotly_chart(fig)
    
    st.subheader('Clustering between Gender and Income')
    k_means_result_4 = pd.read_csv('./K_means_result_4.csv')
    fig = px.line(k_means_result_4, x='Number_of_Clusters', y='K_Means_Score', markers=True, height=800, width=1200, title=('K Means SSE Scores as K Increases'))
    fig.update_layout(title='KMeans SSE Scores as K Increases',
                       yaxis_title='SSE',
                       xaxis_title='Number of Clusters (K)')
    st.plotly_chart(fig)
    
    st.header('DBSCAN')
    
    DBSCAN = pd.read_csv('./Scattered_Plot_for_DBSCAN.csv')
    fig = px.scatter(DBSCAN, x="x", y="y", color='cluster', title='DBSCAN Cluster Result Visualization', labels={'cluster' : 'Cluster', 'x' : 'X', 'y':'Y'}, height=700, width=1200)
    fig.update_layout(legend_orientation="h")
    st.plotly_chart(fig)
    
    st.subheader('Customers in each Clusters')
    fig = px.histogram(DBSCAN, x="cluster", labels=({'cluster' : 'Cluster Number', 'count' : 'Count'}), height=800, width=1200)
    fig.update_layout(bargap=0.2)
    st.plotly_chart(fig)
    
    st.subheader('Age Classification on the basis of the Clusters')
    customer_transaction = pd.read_csv('./customer_transactions.csv')
    fig = px.histogram(customer_transaction, x="age_range", color="Label", height=800, width=1200).update_xaxes(categoryorder='total descending')
    st.plotly_chart(fig)
    
    st.subheader('Income Classification on the basis of the Clusters')
    fig = px.histogram(customer_transaction, x="income", color="Label", height=800, width=1200).update_xaxes(categoryorder='total descending')
    st.plotly_chart(fig)
    
    st.subheader('Customer Days as a Member Classification on the basis of the Clusters')
    fig = px.histogram(customer_transaction, x="days_as_member", color="Label", height=800, width=1200).update_xaxes(categoryorder='total descending')
    st.plotly_chart(fig)
    
    st.subheader('Dataset')
    st.dataframe(customer_transaction)
    
    
    
    
    

if selected == "Sentiment Analysis":
    st.title("Sentiment Analysis")
    
    df = pd.read_csv('./Sentiment_Analysis_Result.csv')
    rate = [5.0, 4.0, 3.0, 2.0, 1.0]
    
    df_2 = pd.read_csv('./Ratings.csv')
    fig = px.histogram(df_2, x="num_rating", color="num_rating", height=800, width=1200, labels={'num_rating' : 'Number of Rating', 'count' : 'Count'})
    fig.update_layout(barmode='stack')
    st.plotly_chart(fig)
    
    
#     def print_confusion_matrix(confusion_matrix, class_names, figsize = (3,3), fontsize=7):
#         df_cm = pd.DataFrame(
#             confusion_matrix, index=class_names, columns=class_names, 
#         )
#         fig = plt.figure(figsize=figsize)
#         try:
#             heatmap = sns.heatmap(df_cm, annot=True, fmt="d")
#         except ValueError:
#             raise ValueError("Confusion matrix values must be integers.")
#         heatmap.yaxis.set_ticklabels(heatmap.yaxis.get_ticklabels(), rotation=0, ha='right', fontsize=fontsize)
#         heatmap.xaxis.set_ticklabels(heatmap.xaxis.get_ticklabels(), rotation=45, ha='right', fontsize=fontsize)
#         plt.ylabel('Truth')
#         plt.xlabel('Prediction')
#         st.pyplot(fig)
#         
#     cm = confusion_matrix(df['y_actual'],df['y_pred'])
#     print_confusion_matrix(cm,rate)
    
    st.subheader('Dataset')
    st.dataframe(df)
    
    
    
    
if selected == "Behaviour Analysis":
    st.title("Behaviour Analysis")
    
    df = pd.read_csv('./Behaviour_Analysis.csv')
    st.subheader('Components of the Customers ( Age, Working Status, Annual Income )')
    fig = px.scatter_3d(df,x='Age', y='Working_Status', z='Annual_Income', labels={'Annual_Income' : 'Annual Income', 'Working_Status' : 'Working Status'}, color='Working_Status',symbol='Gender', height=800, width=1200)
    st.plotly_chart(fig)
    
    st.subheader('Gender Count')
    fig = px.histogram(df, x="Gender", color="Gender", width=1200, height=800)
    fig.update_layout(barmode='stack')
    st.plotly_chart(fig)
    
    st.subheader('Working Status of Customers')
    fig = px.histogram(df, x="Working_Status", color='Working_Status', width=1200, height=800, labels={'count' : 'Count', 'Working_Status' : 'Working Status'})
    fig.update_layout(barmode='stack')
    st.plotly_chart(fig)


    st.subheader('Age Ranges of the Customer')
    fig = px.histogram(df, x="Age", color='Age', width=1200, height=800, labels={'count' : 'Count'})
    fig.update_layout(barmode='stack')
    st.plotly_chart(fig)
    
    st.subheader("Product sales analysis")
    st.text("How much money did people pay to Starbucks during this visit?")
    st.text("")
    dfP = df['Frequently_Purchased'].value_counts()
    dfP = pd.DataFrame({'Product':dfP.index, 'Counts':dfP.values})
    
    layout = go.Layout(width=1200, height=800)
    labels = dfP['Product']
    values = dfP['Counts']
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    fig.update_layout(margin = dict(t=0, l=0, r=0, b=0))
    st.plotly_chart(fig)
            
    
    st.subheader('Visiting Frequency')
    
    fig_1 = px.histogram(df, x="Visit_Frequency", color='Visit_Frequency', width=1200, height=800, labels={'count' : 'Count', 'Visit_Frequency' : 'Visit Frequency'}, title="Histogram Representation")
    fig_1.update_layout(barmode='stack')
    st.plotly_chart(fig_1)
    
    dfF = df['Visit_Frequency'].value_counts()
    dfF = pd.DataFrame({'Frequency':dfF.index, 'Counts':dfF.values})
    
    labels = dfF['Frequency']
    values = dfF['Counts']
    fig_2 = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3, title='Pie Chart Representation')], layout=layout)
    st.plotly_chart(fig_2)
    
    st.subheader('How does people buy starbucks?')
    fig_1 = px.histogram(df, x="Prefered Form of Consumption", color='Prefered Form of Consumption', width=1200, height=800, labels={'count' : 'Count', 'Visit_Frequency' : 'Visit Frequency'}, title="Histogram Representation")
    fig_1.update_layout(barmode='stack')
    st.plotly_chart(fig_1)
    
    
    dfM = df['Prefered Form of Consumption'].value_counts()
    dfM = pd.DataFrame({'Method':dfM.index, 'Counts':dfM.values})
    
    labels = dfM['Method']
    values = dfM['Counts']
    st.text("Pie Chart Representation")
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    st.plotly_chart(fig)
    
    st.subheader("Time spending in Starbucks by each Customers")
    dfT = df['Visit_Time'].value_counts()
    dfT = pd.DataFrame({'Time':dfT.index, 'Counts':dfT.values})
    
    labels = dfT['Time']
    values = dfT['Counts']
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    st.plotly_chart(fig)
    
    st.subheader("Location : Distance of the nearest Starbuck of the customers")
    dfL = df['Nearest_Outlet'].value_counts()
    dfL = pd.DataFrame({'Distance':dfL.index, 'Counts':dfL.values})
    
    labels = dfL['Distance']
    values = dfL['Counts']
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    st.plotly_chart(fig)


    st.subheader("Membership Card: How many customers (don't )have menbership card?")       
    dfMem = df['Membership_Status'].value_counts()
    dfMem = pd.DataFrame({'Membership(Y/N)':dfMem.index, 'Counts':dfMem.values})
    
    labels = dfMem['Membership(Y/N)']
    values = dfMem['Counts']
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    st.plotly_chart(fig)
    
    st.subheader("Money Spend")
    st.text("How much money did people pay to Starbucks during this visit?")
    dfS = df['Avg_Spend'].value_counts()
    dfS = pd.DataFrame({'Costs':dfS.index, 'Counts':dfS.values})
    
    labels = dfS['Costs']
    values = dfS['Counts']
    fig = go.Figure(data=[go.Pie(labels=labels, values=values, hole=.3)], layout=layout)
    fig.update_layout(margin = dict(t=0, l=0, r=0, b=0))
    st.plotly_chart(fig)
    
    st.subheader("Components of customers & Money Spend")
    fig = px.scatter_3d(df,x='Age', y='Working_Status', z='Annual_Income',color='Avg_Spend',symbol='Gender', labels={'Working_Status' : 'Working Status', 'Annual_Income' : 'Annual Income', 'Avg_Spend' : 'Average Spend'}, width=1200, height=800)
    st.plotly_chart(fig)
    
    st.subheader("Which part of starbucks satisfies customers the most/least?")
    Score = {}
    Score['Comparing with other coffee shop']=df['Overall_Rating'].sum()
    Score['Price']=df['Price_Rating'].sum()
    Score['Ambient'] = df['Ambiance_Rating'].sum()
    Score['Wifi'] = df['Wifi_Rating'].sum()
    Score['Service'] = df['Service_Rating'].sum()
    Score['MhRating'] = df['Meeting_Rating'].sum()
    
    # Convert Score into DataFrame
    score_df = pd.DataFrame(Score.items(), columns=['part','score'])
    fig = px.bar(score_df, x='part', y='score', color='part', labels={'part' : 'Part', 'score' : 'Score'}, width=1200, height=800)
    st.plotly_chart(fig)
    
    st.subheader('Dataset')
    st.dataframe(df)
