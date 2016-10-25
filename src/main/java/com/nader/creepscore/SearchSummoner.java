package com.nader.creepscore;

import java.awt.*;
import java.io.IOException;

//Wicket Imports
import com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

//JSON Imports
import org.json.*;

//HTTPClient Imports
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

//Jackson Imports
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

/**
 * Created by nader.baradar on 10/14/2016.
 * This is a simple search functionality.
 * It will accept a summoner name and return information about that summoner
 */
public class SearchSummoner extends WebPage{
    private static final long serialVersionUID = 1L;
    int ajaxCounter;
    Label ajaxLabel, summonerInfo;
    Form summonerSearchForm;
    TextField summonerSearchBox;

    public SearchSummoner(final PageParameters parameters){
        super(parameters);

        //ADD SEARCH BOX: FORM COMPONENT==============================================================
        summonerSearchForm= new Form("summonerSearchForm");
        summonerSearchBox = new TextField("summonerSearchBox", new Model(""));
        summonerSearchForm.add(summonerSearchBox);

        //Add button to form component and override onSubmit method
        summonerSearchForm.add(new Button("summonerSearchButton") {
            @Override
            public void onSubmit(){
                String value = (String)summonerSearchBox.getModelObject();
                String response = null;
                JSONObject responseJSON = new JSONObject();

                //Call RetrieveInfo method using the searched term as the parameter to obtain Summoner JSON
                try {
                    response = RetrieveInfo(value);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Create Object Mapper
                ObjectMapper mapper = new ObjectMapper();
                int count = response.indexOf(':');
                response = response.substring(count+1);
                response = response.substring(0,(response.length()-1));

                //Deserialize JSON String to Object
                try {
                    Summoner summoner = mapper.readValue(response, Summoner.class);

                    System.out.print(summoner);
                }
                catch (JsonParseException e){e.printStackTrace();}
                catch (JsonMappingException e) {e.printStackTrace();}
                catch (IOException e) {e.printStackTrace();}


                //Check to see if RetrieveInfo method successfully responded with desired JSON object
                if (response != null && responseJSON != null) {
                    summonerInfo.setDefaultModelObject(response);
                }else {
                    summonerInfo.setDefaultModelObject(value);
                }

                //Clear the search box
                summonerSearchBox.setModelObject("");
            }
        });

        //Add the form component and label to the page
        add(summonerSearchForm);
        add(summonerInfo = new Label("summonerInfo", new Model("")));

        //ADD AJAX LINK AND DYNAMIC LABEL=============================================================
        add(new AjaxFallbackLink("ajaxCounterLink") {
            @Override
            public void onClick(AjaxRequestTarget target){
                ajaxCounter++;
                if (target != null){
                    target.add(ajaxLabel);
                }
            }
        });
        ajaxLabel = new Label("ajaxLabel", new PropertyModel(this, "ajaxCounter"));
        //must use this to generate a markup identifier for the label, so that the DOM can be updated
        ajaxLabel.setOutputMarkupId(true);
        add(ajaxLabel);

        //ADD HOME BUTTON=============================================================================
        add(new Link("homeLink") {
            @Override
            public void onClick(){
                setResponsePage(HomePage.class);
            }
        });

        //ADD VISIT COUNT LABEL========================================================================
        add(new Label("visitCounter", HomePage.visitCounter));
    }

    /*HTTP REQUEST AND GET==============================================================================
    This method takes a a parameter a summoner name and returns the
        -Summoner ID: long
        -Summoner Name: string
        -Summoner Icon ID: int
        -Summoner Level: long
        -When the summoner was last modified: long */

    public String RetrieveInfo(String summonerName) throws Exception {
        String apiCall = "https://na.api.pvp.net/api/lol/na/v1.4/summoner/by-name/" + summonerName + "?api_key=35e3424c-b67c-4c08-8ed2-5295e9663537";
        String responseBody = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(apiCall);
            System.out.println("Executing request " + httpget.getRequestLine());

            //Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            responseBody = httpclient.execute(httpget, responseHandler);
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    //SUMMONER OBJECT===============================================================
    // Summoner Class to create summoner object and hold
    // information regarding searched summoner
    class Summoner{
        private long summonerId, revisionDate, summonerLevel;
        private int profileIconId;
        private String summonerName;

        @JsonCreator
        public Summoner(){}

        public long getSummonerId(){return summonerId;}
        public void setSummonerId(long summonerId){this.summonerId = summonerId;}

        public String getSummonerName(){return summonerName;}
        public void setSummonerName(String summonerName){this.summonerName = summonerName;}

        public int getProfileIconId(){return profileIconId;}
        public void setProfileIconId(int profileIconId){this.profileIconId = profileIconId;}

        public long getSummonerLevel(){return summonerLevel;}
        public void setSummonerLevel(long summonerLevel){this.summonerLevel = summonerLevel;}

        public long getRevisionDate(){return revisionDate;}
        public void setRevisionDate(long revisionDate){this.revisionDate = revisionDate;}

        public String toString(){
            return "Summoner [ id: "+summonerId+", name: "+summonerName+", level: "+summonerLevel+" ]";
        }
    }
}
