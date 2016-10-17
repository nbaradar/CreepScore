package com.nader.creepscore;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by nader.baradar on 10/14/2016.
 * This is a simple search functionality.
 * It will accept a summoner name and return information based on that summoner
 */
public class SearchSummoner extends WebPage{
    private static final long serialVersionUID = 1L;
    int ajaxCounter;
    Label ajaxLabel;

    public SearchSummoner(final PageParameters parameters){
        super(parameters);
        int visitCount = 0;

        add(new Label("visitCounter", HomePage.visitCounter));
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

        add(new Link("homeLink") {
            @Override
            public void onClick(){
                setResponsePage(HomePage.class);
            }
        });
    }
}
