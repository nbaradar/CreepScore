package com.nader.creepscore;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by nader.baradar on 10/14/2016.
 * This is a simple search functionality.
 * It will accept a summoner name and return information based on that summoner
 */
public class SearchSummoner extends WebPage{
    private static final long serialVersionUID = 1L;

    public SearchSummoner(final PageParameters parameters){
        super(parameters);
        int visitCount = 0;

        add(new Label("countLabel", "Welcome, visitor #" + visitCount + "! Please enter a Summoner Name: "));
    }

}
