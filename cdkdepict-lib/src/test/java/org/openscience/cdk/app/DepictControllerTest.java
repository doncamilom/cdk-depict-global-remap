/*
 * Copyright (c) 2018. NextMove Software Ltd.
 */

package org.openscience.cdk.app;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

class DepictControllerTest {

  @Test
  void getColor() throws Exception {
    Color color = DepictController.getColor("#ff00ff");
    assertThat(color.getRed(), is(255));
    assertThat(color.getGreen(), is(0));
    assertThat(color.getBlue(), is(255));
    assertThat(color.getAlpha(), is(255));
  }

  @Test
  void getColorTruncated() throws Exception {
    Color color = DepictController.getColor("#ff00f");
    assertThat(color.getRed(), is(255));
    assertThat(color.getGreen(), is(0));
    assertThat(color.getBlue(), is(0));
    assertThat(color.getAlpha(), is(255));
  }

  @Test
  void getColorTooLong() throws Exception {
    Color color = DepictController.getColor("#ff00ffffff");
    assertThat(color.getRed(), is(255));
    assertThat(color.getGreen(), is(0));
    assertThat(color.getBlue(), is(255));
    assertThat(color.getAlpha(), is(255));
  }

  @Test
  void mappedReactionPanelReturnsOnlyRequestedSide() throws Exception {
    DepictController controller = new DepictController();
    Map<String, String> params = new HashMap<>();
    params.put("alignrxnmap", "true");
    params.put("panel", "reactants");

    HttpEntity<?> response = controller.depict("[CH3:1][CH2:2][OH:3]>>[CH3:1][CH:2]=[O:3]",
                                               "svg",
                                               "bot",
                                               params);

    String svg = new String((byte[]) response.getBody(), StandardCharsets.UTF_8);
    assertThat(svg, containsString("<svg"));
    assertThat(svg, not(containsString("reaction-arrow")));
  }

  @Test
  void mappedReactionCanStripProductsAfterFullLayout() throws Exception {
    DepictController controller = new DepictController();
    Map<String, String> params = new HashMap<>();
    params.put("alignrxnmap", "true");
    params.put("striprxn", "products");

    HttpEntity<?> response = controller.depict("[CH3:1][CH2:2][OH:3]>>[CH3:1][CH:2]=[O:3]",
                                               "svg",
                                               "bot",
                                               params);

    String svg = new String((byte[]) response.getBody(), StandardCharsets.UTF_8);
    assertThat(svg, containsString("id='mol1'"));
    assertThat(svg, not(containsString("id='mol2'")));
    assertThat(svg, not(containsString("l-4.142")));
  }


}
