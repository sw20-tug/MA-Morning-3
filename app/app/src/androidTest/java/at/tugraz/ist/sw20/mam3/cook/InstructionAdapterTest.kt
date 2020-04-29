package at.tugraz.ist.sw20.mam3.cook

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import at.tugraz.ist.sw20.mam3.cook.model.entities.Step
import at.tugraz.ist.sw20.mam3.cook.ui.add_recipes.adapters.InstructionAdapter
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IndexOutOfBoundsException

@RunWith(AndroidJUnit4::class)
class InstructionAdapterTest {
    private var adapter: InstructionAdapter? = null

    @Before
    fun createAdapter() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val instructions = mutableListOf<Step>().apply {
            add(Step(0, 0, "First step"))
            add(Step(0, 0, "Second step"))
            add(Step(0, 0, "Third step"))
        }
        adapter = InstructionAdapter(context, instructions)
    }

    @Test
    fun testGetAdapter() {
        assertNotNull(adapter)
    }

    @Test
    fun testGetInstructions() {
        assertNotNull(adapter?.instructions)
        assertTrue(adapter!!.instructions.isNotEmpty())
        assertEquals(3, adapter!!.instructions.size)
    }

    @Test
    fun testGetCount() {
        assertEquals(3, adapter?.count)
    }

    @Test
    fun testGetItem() {
        val step0 = adapter?.getItem(0) as Step
        val step1 = adapter?.getItem(1) as Step
        val step2 = adapter?.getItem(2) as Step

        assertNotNull(step0)
        assertNotNull(step1)
        assertNotNull(step2)
        assertEquals("First step", step0.name)
        assertEquals("Second step", step1.name)
        assertEquals("Third step", step2.name)
    }

    @Test
    fun testGetItemId() {
        val id0 = adapter?.getItemId(0)
        val id1 = adapter?.getItemId(1)
        val id2 = adapter?.getItemId(2)

        assertEquals(0, id0?.toInt())
        assertEquals(1, id1?.toInt())
        assertEquals(2, id2?.toInt())
    }

    @Test
    fun testGetInvalidItem() {
        try {
            adapter?.getItem(4) as Step
        }
        catch (ex: IndexOutOfBoundsException) {
            assertTrue(true)
            return
        }
        assertTrue(false)
    }

}