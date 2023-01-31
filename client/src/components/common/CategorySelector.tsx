import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Select, { SingleValue, ActionMeta, PropsValue } from 'react-select';

const CategorySelector = ({
  options,
  onChange,
  targetValue,
}: CategorySelectorPropType) => {
  const [value, setValue] = useState<object | null>(null);

  useEffect(() => {
    if (!targetValue) return;
    if (targetValue?.value === '') {
      setValue(null);
    } else {
      setValue(targetValue);
    }
  }, [targetValue]);

  return (
    <Select
      options={options}
      styles={{
        control: (baseStyles, state) => ({
          ...baseStyles,
          border: `1px solid var(--color-brown100)`,
          height: '42px',
          boxShadow: 'none',
          fontWeight: '400',
        }),
        container: (baseStyles, state) => ({
          ...baseStyles,
          height: '42px',
          padding: '0',
          marginBottom: '10px',
        }),
        valueContainer: (baseStyles, state) => ({
          ...baseStyles,
          height: '42px',
          padding: '0px 8px',
        }),
        input: (baseStyles, state) => ({
          ...baseStyles,
          height: '42px',
        }),
        indicatorSeparator: (baseStyles, state) => ({
          ...baseStyles,
          display: 'none',
        }),
        menu: (baseStyles, state) => ({
          ...baseStyles,
          position: 'relative',
          top: '-10px',
          border: `1px solid var(--color-brown100)`,
          boxShadow: 'none',
        }),
        menuList: (baseStyles, state) => ({
          ...baseStyles,
          border: `1px solid var(--color-brown100)`,
        }),
        dropdownIndicator: (baseStyles, state) => ({
          ...baseStyles,
          transition: 'all .1s ease',
          transform: state.selectProps.menuIsOpen ? 'rotate(180deg)' : 'null',
        }),
        option: (baseStyles, state) => ({
          ...baseStyles,
          height: '42px',
          display: 'flex',
          alignItems: 'center',
          color: state.isSelected
            ? 'var(--color-brown100)'
            : 'var(--color-brown500)',
          backgroundColor: state.isSelected
            ? `var(--color-brown400)`
            : `var(--color-white)`,
        }),
      }}
      placeholder="카테고리를 선택하세요"
      onChange={onChange}
      value={value}
    />
  );
};

type OptionObject = {
  value: string;
  label: string;
};

type CategorySelectorPropType = {
  options: object[];
  onChange: (
    newValue: SingleValue<object>,
    actionMeta: ActionMeta<object>
  ) => void;
  targetValue: OptionObject;
};

export default CategorySelector;
