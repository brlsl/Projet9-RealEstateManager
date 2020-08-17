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
    private static long AGENT_ID = 42;
    private static Agent AGENT_DEMO = new Agent(AGENT_ID,"ABC","Br");
    private static long PROPERTY_ID = 456;
    private static Property PROPERTY_DEMO = new Property(AGENT_ID, "Paris", "123");
    private static String IMAGE_PATH = "storage/fakePath/";
    private static Image IMAGE_DEMO = new Image(1,PROPERTY_ID, IMAGE_PATH);

    // ----- AGENT DAO TESTS -----

    @Test
    public void getAgentListWhenNoAgentInserted() throws InterruptedException {
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertTrue(agentList.isEmpty());
    }

    @Test
    public void insertAndGetAgent() throws InterruptedException {
        // BEFORE : add agent in db
        this.database.agentDao().createAgent(AGENT_DEMO);
        // TEST
        Agent agent = LiveDataTestUtil.getValue(this.database.agentDao().getAgent(AGENT_ID));
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertTrue(agent.getId() == (AGENT_DEMO.getId())
                && agent.getName().equals(AGENT_DEMO.getName())
                && agent.getSurname().equals(AGENT_DEMO.getSurname())
                && agentList.size() == 1);
    }

    @Test
    public void insertAndUpdateAgent() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO);
        Agent agent = LiveDataTestUtil.getValue(this.database.agentDao().getAgent(AGENT_ID));
        agent.setSurname("Bruno");
        this.database.agentDao().updateAgent(agent);
        // TEST
        Agent agentUpdated = LiveDataTestUtil.getValue(database.agentDao().getAgent(AGENT_ID));
        assertEquals("Bruno", agentUpdated.getSurname());
    }

    @Test
    public void insertAndDeleteAgent() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO);
        List<Agent> agentList = LiveDataTestUtil.getValue(this.database.agentDao().getAgentList());
        assertEquals(1, agentList.size());
        // TEST
        this.database.agentDao().deleteAgent(AGENT_DEMO);
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
        this.database.agentDao().createAgent(AGENT_DEMO);
        this.database.propertyDao().createProperty(PROPERTY_DEMO); // add property in DB

        Property property = LiveDataTestUtil.getValue(database.propertyDao().getProperty(AGENT_ID));
        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue(property.getAgentId() == AGENT_DEMO.getId()
                && property.getCity().equals(PROPERTY_DEMO.getCity())
                && property.getPrice().equals(PROPERTY_DEMO.getPrice())
                && propertyList.size() == 1);
    }

    @Test
    public void insertAndUpdateProperty() throws InterruptedException{
        this.database.agentDao().createAgent(AGENT_DEMO);
        this.database.propertyDao().createProperty(PROPERTY_DEMO);
        Property property = LiveDataTestUtil.getValue(database.propertyDao().getProperty(AGENT_ID));
        property.setCity("Marseille");
        property.setPrice("456");
        this.database.propertyDao().updateProperty(property);

        List<Property> propertyList = LiveDataTestUtil.getValue(database.propertyDao().getPropertyList());
        assertTrue(propertyList.size() == 1
                && propertyList.get(0).getCity().equals("Marseille")
                && propertyList.get(0).getPrice().equals("456"));
    }



    // ------ IMAGE TESTS -----

    @Test
    public void getImageListWhenNoImageInserted() throws InterruptedException {
        List<Image> imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageList());
        assertEquals(0, imageList.size());
    }

    @Test
    public void insertAndGetImage() throws InterruptedException {

        this.database.imageDao().createImage(IMAGE_DEMO);
        Image image = LiveDataTestUtil.getValue(this.database.imageDao().getImage(PROPERTY_ID));
        assertEquals(IMAGE_PATH, image.getImagePath());
    }


    @Test
    public void insertAndUpdateImage() throws InterruptedException {
        this.database.imageDao().createImage(IMAGE_DEMO);

        Image image = LiveDataTestUtil.getValue(this.database.imageDao().getImage(PROPERTY_ID));
        image.setImagePath("storage/pathUpdated");

        this.database.imageDao().updateImage(image);

        List<Image> imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageList());
        assertTrue(imageList.size() == 1
                && imageList.get(0).getId() == image.getId()
                && imageList.get(0).getImagePath().equals(image.getImagePath()));
    }

    @Test
    public void insertAndDeleteImage() throws InterruptedException {
        this.database.imageDao().createImage(IMAGE_DEMO);
        List<Image> imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageList());
        assertEquals(1, imageList.size());

        // TEST
        this.database.imageDao().deleteImage(IMAGE_DEMO);
        imageList = LiveDataTestUtil.getValue(this.database.imageDao().getImageList());
        assertEquals(0, imageList.size());

    }

}