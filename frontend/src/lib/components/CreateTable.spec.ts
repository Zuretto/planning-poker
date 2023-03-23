import {describe, it, expect, beforeEach, vi} from 'vitest';
import type {Mock} from 'vitest';
import {render, screen, fireEvent, cleanup} from '@testing-library/svelte';
import type {RenderResult} from '@testing-library/svelte';
import CreateTable from "./CreateTable.svelte";


describe('<CreateTable/>', () => {

    let renderResult: RenderResult<CreateTable>;

    vi.mock('$app/navigation', () => ({
        goto: vi.fn(),
    }));

    beforeEach(() => {
        cleanup();
        renderResult = render(CreateTable);
        global.fetch = vi.fn();
        (global.fetch as Mock).mockResolvedValue({json: () => new Promise(() => ({id: 'test'}))});
    });

    it('should bind value from input element', async () => {
        // given
        const input = screen.getByLabelText('Your nickname') as HTMLInputElement;
        await fireEvent.input(input, {target: {value: 'John Doe'}});
        // when
        const button = screen.getByRole('button');
        await fireEvent.click(button);
        // then
        expect(fetch).toHaveBeenCalledOnce();
    });

    it('should not make a request if the username is blank', async () => {
        // given
        const input = screen.getByLabelText('Your nickname') as HTMLInputElement;
        await fireEvent.input(input, {target: {value: '  '}});
        // when
        const button = screen.getByRole('button');
        await fireEvent.click(button);
        // then
        expect(fetch).not.toHaveBeenCalled();
    });
});