package br.com.caelum.vraptor.server;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.learn.dao.TagDAO;
import org.learn.dao.TagDictionaryDAO;

import br.com.caelum.vraptor.events.VRaptorInitialized;

@ApplicationScoped
public class ApplicationInitilizer {
	
	@Inject 
	private TagDictionaryDAO tags;
	
	public void observesBootstrap(@Observes VRaptorInitialized event) {
        System.out.println("Prepare Tag List");
        tags.prepareTagList();
    }

}
