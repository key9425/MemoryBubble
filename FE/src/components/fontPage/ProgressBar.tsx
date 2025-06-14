import React from 'react';
import ProgressStep from './ProgressStep';
import { Step } from '@/hooks/useFontStep';

interface ProgressBarProps {
  steps: Step[];
  currentStep: number;
}

const ProgressBar: React.FC<ProgressBarProps> = ({ steps, currentStep }) => {
  return (
    <div className="flex w-full gap-1">
      {steps.map((step) => {
        const isActive = step.id <= currentStep;

        return <ProgressStep key={step.id} step={step} isActive={isActive} />;
      })}
    </div>
  );
};

export default ProgressBar;
