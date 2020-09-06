package com.openclassrooms.realestatemanager;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase;
import com.openclassrooms.realestatemanager.models.Agent;
import com.openclassrooms.realestatemanager.models.Image;
import com.openclassrooms.realestatemanager.models.Property;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    // FOR DATA
    private RealEstateManagerDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                RealEstateManagerDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }


    // DATA SET FOR TESTS
    private static long AGENT_ID_1 = 42;
    private static long AGENT_ID_2 = 654;
    private static Agent AGENT_DEMO_1 = new Agent(AGENT_ID_1,"ABC","Br");
    private static Agent AGENT_DEMO_2 = new Agent(AGENT_ID_2,"ABC","Br");

    private static long PROPERTY_ID_1 = 123456;
    private static long PROPERTY_ID_2 = 987654;
    private static Property PROPERTY_DEMO_1 = new Property(PROPERTY_ID_1, AGENT_ID_1, "Paris", "123");
    private static Property PROPERTY_DEMO_2 = new Property(PROPERTY_ID_2, AGENT_ID_1, "Issy", "456");

    private static String IMAGE_PATH_1 = "storage/fakePath1/";
    private static String IMAGE_PATH_2 = "storage/fakePath1/";
    private static long IMAGE_ID_1 = 147;
    private static long IMAGE_ID_2 = 963;
    private static Image IMAGE_DEMO_1 = new Image(IMAGE_ID_1, PROPERTY_ID_1, IMAGE_PATH_1);
    private static Image IMAGE_DEMO_2 = new Image(IMAGE_ID_2, PROPERTY_ID_2, IMAGE_PATH_2);

    // ----- AGENT DAO TESTS -----

    @Test
    public void getAgentListWhenNoAgentInserted() throws InterruptedException {
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertTrue(agentList.isEmpty());
    }

    @Test
    public void insertAndGetAgent() throws InterruptedException {
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        this.database.agentDao().createAgent(AGENT_DEMO_2);
        // TEST
        Agent agent = LiveDataTestUtil.getValue(this.database.agentDao().getAgent(AGENT_ID_1));
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertTrue(agent.getId() == (AGENT_DEMO_1.getId())
                && agent.getName().equals(AGENT_DEMO_1.getName())
                && agent.getSurname().equals(AGENT_DEMO_1.getSurname())
                && agentList.size() == 2);
    }

    @Test
    public void insertAndUpdateAgent() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        Agent agent = LiveDataTestUtil.getValue(this.database.agentDao().getAgent(AGENT_ID_1));
        agent.setSurname("Bruno");
        this.database.agentDao().updateAgent(agent);
        // TEST
        Agent agentUpdated = LiveDataTestUtil.getValue(database.agentDao().getAgent(AGENT_ID_1));
        assertEquals("Bruno", agentUpdated.getSurname());
    }

    @Test
    public void insertAndDeleteAgent() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertEquals(1, agentList.size());
        // TEST
        this.database.agentDao().deleteAgent(AGENT_DEMO_1);
        agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertTrue(agentList.isEmpty());
    }

    // ----- PROPERTY DAO TESTS -----

    @Test
    public void getPropertyListWhenNoPropertyInserted() throws InterruptedException {
        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue(propertyList.isEmpty());
    }

    @Test
    public void insertAndGetProperty() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        this.database.propertyDao().createProperty(PROPERTY_DEMO_1);
        this.database.propertyDao().createProperty(PROPERTY_DEMO_2);
        // TEST
        Property property = LiveDataTestUtil.getValue(database.propertyDao().getProperty(2, AGENT_ID_1));
        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue(property.getAgentId() == AGENT_DEMO_1.getId()
                && property.getId() == PROPERTY_DEMO_2.getId()
                && property.getCity().equals(PROPERTY_DEMO_2.getCity())
                && property.getPrice().equals(PROPERTY_DEMO_2.getPrice())
                && propertyList.size() == 2);

    }
/*
    @Test
    public void insertAndUpdateProperty() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        this.database.propertyDao().createProperty(PROPERTY_DEMO_1);
        this.database.propertyDao().createProperty(PROPERTY_DEMO_2);
        Property property = LiveDataTestUtil.getValue(database.propertyDao().getProperty(PROPERTY_ID_1, AGENT_ID_1));
        Property property2 = LiveDataTestUtil.getValue(database.propertyDao().getProperty(PROPERTY_ID_2, AGENT_ID_1));
        property.setCity("Marseille");
        property.setPrice("789");
        // TEST
        this.database.propertyDao().updateProperty(property);
        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue(propertyList.size() == 1
                && propertyList.get(0).getCity().equals("Marseille")
                && propertyList.get(0).getPrice().equals("789"));
    }

 */

    @Test
    public void insertAndUpdateProperty() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO_1);
        this.database.propertyDao().createProperty(PROPERTY_DEMO_1);

        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());;
        assertTrue( propertyList.size() == 1
                && propertyList.get(0).getCity().equals("Paris")
                && propertyList.get(0).getPrice().equals("123"));
        //TEST
        this.database.propertyDao().updatePropertyTest("St-Louis", "444" , PROPERTY_ID_1);
        propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue( propertyList.size() == 1
                && propertyList.get(0).getCity().equals("St-Louis")
                && propertyList.get(0).getPrice().equals("444"));

    }


    // ------ IMAGE TESTS -----

    @Test
    public void getImageListWhenNoImageInserted() throws InterruptedException {
        List<Image> imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageListAllProperties());
        assertEquals(0, imageList.size());
    }

    @Test
    public void insertAndGetImage() throws InterruptedException {
        this.database.imageDao().createImage(IMAGE_DEMO_1);
        this.database.imageDao().createImage(IMAGE_DEMO_2);
        //TEST
        Image image = LiveDataTestUtil.getValue(this.database.imageDao().getImage(IMAGE_ID_2, PROPERTY_ID_2));
        List<Image> imageListAllProperties = LiveDataTestUtil.getValue(this.database.imageDao().getImageListAllProperties());
        List<Image> imageListProperty = LiveDataTestUtil.getValue(this.database.imageDao().getImageListOfOneProperty(PROPERTY_ID_2));
        assertTrue(IMAGE_DEMO_2.getImagePath().equals(image.getImagePath())
                && IMAGE_DEMO_2.getId() == image.getId()
                && imageListAllProperties.size() == 2
                && imageListProperty.size() == 1);
    }

    @Test
    public void insertAndUpdateImage() throws InterruptedException {
        this.database.imageDao().createImage(IMAGE_DEMO_1);
        Image image = LiveDataTestUtil.getValue(this.database.imageDao().getImage(IMAGE_ID_1, PROPERTY_ID_1));
        image.setImagePath("storage/originalPath");
        // TEST
        this.database.imageDao().updateImage("storage/updatedPath", IMAGE_ID_1);
        Image imageUpdated = LiveDataTestUtil.getValue(this.database.imageDao().getImage(IMAGE_ID_1, PROPERTY_ID_1));
        assertTrue(imageUpdated.getId() == IMAGE_ID_1
                && imageUpdated.getImagePath().equals("storage/updatedPath"));
    }

    @Test
    public void insertAndDeleteImage() throws InterruptedException {
        this.database.imageDao().createImage(IMAGE_DEMO_1);
        this.database.imageDao().createImage(IMAGE_DEMO_2);
        Image image = new Image(3, PROPERTY_ID_1, IMAGE_PATH_1);
        this.database.imageDao().createImage(image);
        List<Image> imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageListAllProperties());
        assertEquals(3, imageList.size());
        // TEST
        this.database.imageDao().deleteImagesOneProperty(PROPERTY_ID_1);
        imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageListAllProperties());
        assertEquals(1, imageList.size());
    }
}
