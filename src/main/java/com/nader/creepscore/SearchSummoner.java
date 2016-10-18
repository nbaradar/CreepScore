package com.nader.creepscore;

import java.awt.*;

//Wicket Imports
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
        int visitCount = 0;

        //ADD VISIT COUNT LABEL
        add(new Label("visitCounter", HomePage.visitCounter));

        //ADD FORM COMPONENT
        summonerSearchForm= new Form("summonerSearchForm");
        summonerSearchBox = new TextField("summonerSearchBox", new Model(""));
        summonerSearchForm.add(summonerSearchBox);
        summonerSearchForm.add(new Button("summonerSearchButton") {
            @Override
            public void onSubmit(){
                String value = (String)summonerSearchBox.getModelObject();
                summonerInfo.setDefaultModelObject(value);
                summonerSearchBox.setModelObject("");
            }
        });
        add(summonerSearchForm);
        add(summonerInfo = new Label("summonerInfo", new Model("")));

        //ADD AJAX LINK AND DYNAMIC LABEL
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
        ajaxLabel.setOutputMarkupId(true);
        add(ajaxLabel);

        //ADD HOME BUTTON
        add(new Link("homeLink") {
            @Override
            public void onClick(){
                setResponsePage(HomePage.class);
            }
        });
    }
}
