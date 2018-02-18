package eladkay.quaeritum.client.gui.book;

import com.google.gson.JsonElement;
import com.teamwizardry.librarianlib.features.animator.Easing;
import com.teamwizardry.librarianlib.features.animator.animations.BasicAnimation;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.component.GuiComponentEvents;
import eladkay.quaeritum.api.book.hierarchy.category.Category;
import kotlin.Unit;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;

public class ComponentCategory extends BookGuiComponent {

    private final Category category;

    public ComponentCategory(int posX, int posY, int width, int height, GuiBook book, @Nonnull Category category) {
        super(posX, posY, width, height, book, book.MAIN_INDEX);
        this.category = category;

        JsonElement icon = category.icon;
        String title = I18n.format(category.titleKey);
        String description = I18n.format(category.descKey);

        BookGuiComponent linkComponent = new ComponentIndexPage(book, book.MAIN_INDEX, category);

        book.COMPONENT_BOOK.add(linkComponent);
        linkComponent.setVisible(false);

        BUS.hook(GuiComponentEvents.MouseClickEvent.class, (event) -> {
            if (getLinkingParent() != null) {
                linkComponent.setLinkingParent(getLinkingParent());
                book.FOCUSED_COMPONENT.setVisible(false);
                book.FOCUSED_COMPONENT = linkComponent;
                book.FOCUSED_COMPONENT.setVisible(true);
            }
        });

        // ------- BUTTON RENDERING AND ANIMATION ------- //
        Runnable iconMask = getRendererFor(icon, true);
        {
            BUS.hook(GuiComponentEvents.PostDrawEvent.class, (GuiComponentEvents.PostDrawEvent event) -> {
                GlStateManager.color(0, 0, 0);
                iconMask.run();
            });

            render.getTooltip().func((Function<GuiComponent, List<String>>) guiComponent -> {
                List<String> list = new ArrayList<>();
                list.add(title);
                list.add(TextFormatting.GRAY + description);
                return list;
            });

            ComponentAnimatableVoid circleWipe = new ComponentAnimatableVoid(0, 0, 24, 24);
            add(circleWipe);
            circleWipe.getTransform().setTranslateZ(100);

            circleWipe.clipping.setClipToBounds(true);
            circleWipe.clipping.setCustomClipping(() -> {

                GlStateManager.disableTexture2D();
                GlStateManager.disableCull();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder buffer = tessellator.getBuffer();
                buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
                for (int i = 0; i <= 10; i++) {
                    float angle = (float) (i * Math.PI * 2 / 10);
                    float x1 = (float) (12 + MathHelper.cos(angle) * circleWipe.animX);
                    float y1 = (float) (12 + MathHelper.sin(angle) * circleWipe.animX);
                    buffer.pos(x1, y1, 100).color(0f, 1f, 1f, 1f).endVertex();
                }
                tessellator.draw();

                return Unit.INSTANCE;
            });

            final double radius = 16;

            circleWipe.BUS.hook(GuiComponentEvents.MouseInEvent.class, event -> {

                BasicAnimation mouseInAnim = new BasicAnimation<>(circleWipe, "animX");
                mouseInAnim.setDuration(10);
                mouseInAnim.setEasing(Easing.easeOutQuint);
                mouseInAnim.setTo(radius);
                event.component.add(mouseInAnim);
            });

            circleWipe.BUS.hook(GuiComponentEvents.MouseOutEvent.class, event -> {

                BasicAnimation mouseOutAnim = new BasicAnimation<>(circleWipe, "animX");
                mouseOutAnim.setDuration(10);
                mouseOutAnim.setEasing(Easing.easeOutQuint);
                mouseOutAnim.setTo(0);
                event.component.add(mouseOutAnim);
            });

            circleWipe.BUS.hook(GuiComponentEvents.PostDrawEvent.class, (GuiComponentEvents.PostDrawEvent event) -> {
                GlStateManager.color(book.highlightColor.getRed(), book.highlightColor.getGreen(), book.highlightColor.getBlue());
                GlStateManager.enableAlpha();
                GlStateManager.disableCull();
                GL11.glEnable(GL_POLYGON_SMOOTH);
                iconMask.run();
                GL11.glDisable(GL_POLYGON_SMOOTH);
                GlStateManager.enableCull();
            });
        }
        // ------- BUTTON RENDERING AND ANIMATION ------- //
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Nullable
    @Override
    public JsonElement getIcon() {
        return null;
    }

    @Override
    public void update() {

    }

    @Nonnull
    @Override
    public BookGuiComponent clone() {
        return new ComponentCategory(getPos().getXi(), getPos().getYi(), getSize().getXi(), getSize().getYi(), getBook(), category);
    }
}
