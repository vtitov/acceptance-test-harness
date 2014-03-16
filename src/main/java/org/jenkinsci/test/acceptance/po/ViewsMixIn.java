package org.jenkinsci.test.acceptance.po;

import com.google.inject.Injector;

import java.net.URL;

/**
 * Mix-in for {@link PageObject}s that own a group of views, like
 * {@link Jenkins}.
 *
 * @author Kohsuke Kawaguchi
 */
public class ViewsMixIn extends MixIn {
    public ViewsMixIn(ContainerPageObject context) {
        super(context);
    }

    public <T extends View> T create(Class<T> type, String name) {
        String sut_type = type.getAnnotation(ViewPageObject.class).value();

        visit("newView");
        fillIn("name",name);
        check(find(by.radioButton(sut_type)));
        clickButton("OK");

        try {
            return type.getConstructor(Injector.class,URL.class)
                    .newInstance(injector, url("view/%s/", name));
        } catch (ReflectiveOperationException e) {
            throw new AssertionError(e);
        }
    }

}